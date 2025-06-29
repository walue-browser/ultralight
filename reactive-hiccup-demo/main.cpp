#include <AppCore/App.h>
#include <AppCore/Window.h>
#include <AppCore/Overlay.h>
#include <AppCore/JSHelpers.h>
#include <memory>
#include <chrono>
#include <thread>
#include <atomic>
#include <mutex>
#include <string>

using namespace ultralight;

///
/// Reactive Hiccup Demo
///
/// This sample demonstrates how to create a reactive UI with Clojure Hiccup
/// and integrate it with Ultralight for real-time updates.
///

class HiccupWindow : public WindowListener,
                     public ViewListener,
                     public LoadListener {
  RefPtr<Window> window_;
  RefPtr<Overlay> overlay_;
  std::atomic<bool> is_running_;
  std::mutex update_mutex_;
  std::string current_hiccup_html_;

public:
  HiccupWindow(const char* title, const char* url, int x, int y, int width, int height) {
    window_ = Window::Create(App::instance()->main_monitor(), width, height, false,
      kWindowFlags_Titled | kWindowFlags_Resizable | kWindowFlags_Hidden);
    window_->MoveTo(x, y);
    window_->SetTitle(title);
    window_->Show();
    window_->set_listener(this);

    overlay_ = Overlay::Create(window_, window_->width(), window_->height(), 0, 0);
    overlay_->view()->set_view_listener(this);
    overlay_->view()->set_load_listener(this);
    overlay_->view()->LoadURL(url);
    
    is_running_ = true;
    current_hiccup_html_ = "";
  }

  ~HiccupWindow() {
    is_running_ = false;
  }

  inline RefPtr<View> view() { return overlay_->view(); }
  inline RefPtr<Window> window() { return window_; }
  inline RefPtr<Overlay> overlay() { return overlay_; }
  
  bool is_running() const { return is_running_; }

  void update_hiccup_content(const std::string& html_content) {
    std::lock_guard<std::mutex> lock(update_mutex_);
    current_hiccup_html_ = html_content;
    
    // Update the view with the new HTML content
    if (is_running_) {
      RefPtr<JSContext> context = view()->LockJSContext();
      SetJSContext(context->ctx());
      JSObject global = JSGlobalObject();
      global["hiccup_content"] = current_hiccup_html_;
      view()->EvaluateScript("updateHiccupContent(hiccup_content);");
    }
  }

  virtual void OnClose(ultralight::Window* window) override {
    is_running_ = false;
    App::instance()->Quit();
  }

  virtual void OnResize(ultralight::Window* window, uint32_t width, uint32_t height) override {
    overlay_->Resize(width, height);
  }

  virtual void OnChangeCursor(ultralight::View* caller, ultralight::Cursor cursor) override {
    window_->SetCursor(cursor);
  }
  
  virtual void OnDOMReady(ultralight::View* caller, uint64_t frame_id, bool is_main_frame,
    const String& url) override {
    // Set up JavaScript context
    RefPtr<JSContext> context = caller->LockJSContext();
    SetJSContext(context->ctx());
    
    // Get the global object
    JSObject global = JSGlobalObject();
    
    // Bind our native callback to JavaScript
    global["notifyHiccupUpdate"] = BindJSCallbackWithRetval(&HiccupWindow::NotifyHiccupUpdate);
    
    // Initialize with current content if available
    if (!current_hiccup_html_.empty()) {
      global["hiccup_content"] = current_hiccup_html_;
      caller->EvaluateScript("updateHiccupContent(hiccup_content);");
    }
  }
  
  JSValue NotifyHiccupUpdate(const JSObject& thisObject, const JSArgs& args) {
    if (args.size() >= 1) {
      std::string message = args[0].ToString();
      printf("Hiccup update notification: %s\n", message.c_str());
    }
    return JSValue();
  }
};

class HiccupEditorWindow : public HiccupWindow {
  HiccupWindow* preview_window_;
  
public:
  HiccupEditorWindow(const char* title, const char* url, int x, int y, int width, int height)
    : HiccupWindow(title, url, x, y, width, height), preview_window_(nullptr) {
  }
  
  void set_preview_window(HiccupWindow* preview) {
    preview_window_ = preview;
  }
  
  virtual void OnDOMReady(ultralight::View* caller, uint64_t frame_id, bool is_main_frame,
    const String& url) override {
    // Call parent implementation first
    HiccupWindow::OnDOMReady(caller, frame_id, is_main_frame, url);
    
    // Set up additional JavaScript bindings for the editor
    RefPtr<JSContext> context = caller->LockJSContext();
    SetJSContext(context->ctx());
    JSObject global = JSGlobalObject();
    
    // Bind the update function
    global["updateHiccupPreview"] = BindJSCallbackWithRetval(&HiccupEditorWindow::UpdateHiccupPreview);
    
    // Initialize the editor
    caller->EvaluateScript("var content = editor.getSession().getValue(); updateHiccupPreview(content);");
  }
  
  JSValue UpdateHiccupPreview(const JSObject& thisObject, const JSArgs& args) {
    if (preview_window_ && args.size() == 1) {
      std::string content = args[0].ToString();
      preview_window_->update_hiccup_content(content);
    }
    return JSValue();
  }
};

class ReactiveHiccupDemo {
  RefPtr<App> app_;
  std::unique_ptr<HiccupEditorWindow> editor_window_;
  std::unique_ptr<HiccupWindow> preview_window_;
  
public:
  ReactiveHiccupDemo() {
    // Create the main App instance
    app_ = App::Create();
    
    // Create the preview window first
    preview_window_.reset(new HiccupWindow(
      "Hiccup Preview", "file:///preview.html", 700, 50, 600, 700));
    
    // Create the editor window
    editor_window_.reset(new HiccupEditorWindow(
      "Hiccup Editor", "file:///editor.html", 50, 50, 600, 700));
    
    // Connect the windows
    editor_window_->set_preview_window(preview_window_.get());
  }
  
  void Run() {
    app_->Run();
  }
};

int main() {
  ReactiveHiccupDemo demo;
  demo.Run();
  
  return 0;
}