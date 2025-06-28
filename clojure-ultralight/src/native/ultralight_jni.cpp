#include <jni.h>
#include <AppCore/App.h>
#include <AppCore/Window.h>
#include <AppCore/Overlay.h>
#include <iostream>
#include <memory>
#include <map>

using namespace ultralight;

// Global storage for app instances
static std::map<jlong, RefPtr<App>> apps;
static std::map<jlong, RefPtr<Window>> windows;
static std::map<jlong, RefPtr<Overlay>> overlays;

// Window listener class to handle events
class JNIWindowListener : public WindowListener {
public:
    virtual void OnClose(ultralight::Window* window) override {
        // Find the app associated with this window and quit it
        for (auto& pair : apps) {
            pair.second->Quit();
        }
    }
    
    virtual void OnResize(ultralight::Window* window, uint32_t width, uint32_t height) override {
        // Handle resize if needed
    }
};

// View listener class to handle cursor changes
class JNIViewListener : public ViewListener {
private:
    RefPtr<Window> window_;
    
public:
    JNIViewListener(RefPtr<Window> window) : window_(window) {}
    
    virtual void OnChangeCursor(ultralight::View* caller, ultralight::Cursor cursor) override {
        if (window_) {
            window_->SetCursor(cursor);
        }
    }
};

extern "C" {

JNIEXPORT jlong JNICALL
Java_com_ultralight_UltralightApp_createApp(JNIEnv *env, jobject obj) {
    try {
        RefPtr<App> app = App::Create();
        if (!app) {
            return 0;
        }
        
        jlong appPtr = reinterpret_cast<jlong>(app.get());
        apps[appPtr] = app;
        
        std::cout << "Created Ultralight app: " << appPtr << std::endl;
        return appPtr;
    } catch (const std::exception& e) {
        std::cerr << "Error creating app: " << e.what() << std::endl;
        return 0;
    }
}

JNIEXPORT jlong JNICALL
Java_com_ultralight_UltralightApp_createWindow(JNIEnv *env, jobject obj, jlong appPtr, jint width, jint height, jstring title) {
    try {
        auto appIt = apps.find(appPtr);
        if (appIt == apps.end()) {
            std::cerr << "Invalid app pointer" << std::endl;
            return 0;
        }
        
        RefPtr<App> app = appIt->second;
        RefPtr<Window> window = Window::Create(app->main_monitor(), width, height, false, kWindowFlags_Titled);
        
        if (!window) {
            std::cerr << "Failed to create window" << std::endl;
            return 0;
        }
        
        // Set window title
        const char* titleStr = env->GetStringUTFChars(title, nullptr);
        window->SetTitle(titleStr);
        env->ReleaseStringUTFChars(title, titleStr);
        
        // Set window listener
        static JNIWindowListener windowListener;
        window->set_listener(&windowListener);
        
        jlong windowPtr = reinterpret_cast<jlong>(window.get());
        windows[windowPtr] = window;
        
        std::cout << "Created window: " << windowPtr << " (" << width << "x" << height << ")" << std::endl;
        return windowPtr;
    } catch (const std::exception& e) {
        std::cerr << "Error creating window: " << e.what() << std::endl;
        return 0;
    }
}

JNIEXPORT jlong JNICALL
Java_com_ultralight_UltralightApp_createOverlay(JNIEnv *env, jobject obj, jlong windowPtr, jint width, jint height) {
    try {
        auto windowIt = windows.find(windowPtr);
        if (windowIt == windows.end()) {
            std::cerr << "Invalid window pointer" << std::endl;
            return 0;
        }
        
        RefPtr<Window> window = windowIt->second;
        RefPtr<Overlay> overlay = Overlay::Create(window, width, height, 0, 0);
        
        if (!overlay) {
            std::cerr << "Failed to create overlay" << std::endl;
            return 0;
        }
        
        // Set view listener
        static std::unique_ptr<JNIViewListener> viewListener = std::make_unique<JNIViewListener>(window);
        overlay->view()->set_view_listener(viewListener.get());
        
        jlong overlayPtr = reinterpret_cast<jlong>(overlay.get());
        overlays[overlayPtr] = overlay;
        
        std::cout << "Created overlay: " << overlayPtr << std::endl;
        return overlayPtr;
    } catch (const std::exception& e) {
        std::cerr << "Error creating overlay: " << e.what() << std::endl;
        return 0;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_loadHTML(JNIEnv *env, jobject obj, jlong overlayPtr, jstring html) {
    try {
        auto overlayIt = overlays.find(overlayPtr);
        if (overlayIt == overlays.end()) {
            std::cerr << "Invalid overlay pointer" << std::endl;
            return;
        }
        
        const char* htmlStr = env->GetStringUTFChars(html, nullptr);
        RefPtr<Overlay> overlay = overlayIt->second;
        overlay->view()->LoadHTML(htmlStr);
        
        std::cout << "Loaded HTML content into overlay: " << overlayPtr << std::endl;
        env->ReleaseStringUTFChars(html, htmlStr);
    } catch (const std::exception& e) {
        std::cerr << "Error loading HTML: " << e.what() << std::endl;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_loadURL(JNIEnv *env, jobject obj, jlong overlayPtr, jstring url) {
    try {
        auto overlayIt = overlays.find(overlayPtr);
        if (overlayIt == overlays.end()) {
            std::cerr << "Invalid overlay pointer" << std::endl;
            return;
        }
        
        const char* urlStr = env->GetStringUTFChars(url, nullptr);
        RefPtr<Overlay> overlay = overlayIt->second;
        overlay->view()->LoadURL(urlStr);
        
        std::cout << "Loaded URL into overlay: " << overlayPtr << " -> " << urlStr << std::endl;
        env->ReleaseStringUTFChars(url, urlStr);
    } catch (const std::exception& e) {
        std::cerr << "Error loading URL: " << e.what() << std::endl;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_runApp(JNIEnv *env, jobject obj, jlong appPtr) {
    try {
        auto appIt = apps.find(appPtr);
        if (appIt == apps.end()) {
            std::cerr << "Invalid app pointer" << std::endl;
            return;
        }
        
        RefPtr<App> app = appIt->second;
        std::cout << "Running Ultralight app: " << appPtr << std::endl;
        app->Run();
        std::cout << "App finished running: " << appPtr << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "Error running app: " << e.what() << std::endl;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_destroyApp(JNIEnv *env, jobject obj, jlong appPtr) {
    try {
        // Clean up overlays
        for (auto it = overlays.begin(); it != overlays.end();) {
            it = overlays.erase(it);
        }
        
        // Clean up windows
        for (auto it = windows.begin(); it != windows.end();) {
            it = windows.erase(it);
        }
        
        // Clean up app
        auto appIt = apps.find(appPtr);
        if (appIt != apps.end()) {
            apps.erase(appIt);
            std::cout << "Destroyed app: " << appPtr << std::endl;
        }
    } catch (const std::exception& e) {
        std::cerr << "Error destroying app: " << e.what() << std::endl;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_setWindowTitle(JNIEnv *env, jobject obj, jlong windowPtr, jstring title) {
    try {
        auto windowIt = windows.find(windowPtr);
        if (windowIt == windows.end()) {
            std::cerr << "Invalid window pointer" << std::endl;
            return;
        }
        
        const char* titleStr = env->GetStringUTFChars(title, nullptr);
        RefPtr<Window> window = windowIt->second;
        window->SetTitle(titleStr);
        
        std::cout << "Set window title: " << titleStr << std::endl;
        env->ReleaseStringUTFChars(title, titleStr);
    } catch (const std::exception& e) {
        std::cerr << "Error setting window title: " << e.what() << std::endl;
    }
}

JNIEXPORT void JNICALL
Java_com_ultralight_UltralightApp_evaluateScript(JNIEnv *env, jobject obj, jlong overlayPtr, jstring script) {
    try {
        auto overlayIt = overlays.find(overlayPtr);
        if (overlayIt == overlays.end()) {
            std::cerr << "Invalid overlay pointer" << std::endl;
            return;
        }
        
        const char* scriptStr = env->GetStringUTFChars(script, nullptr);
        RefPtr<Overlay> overlay = overlayIt->second;
        overlay->view()->EvaluateScript(scriptStr);
        
        std::cout << "Executed script: " << scriptStr << std::endl;
        env->ReleaseStringUTFChars(script, scriptStr);
    } catch (const std::exception& e) {
        std::cerr << "Error evaluating script: " << e.what() << std::endl;
    }
}

JNIEXPORT jstring JNICALL
Java_com_ultralight_UltralightApp_getScriptResult(JNIEnv *env, jobject obj, jlong overlayPtr, jstring script) {
    try {
        auto overlayIt = overlays.find(overlayPtr);
        if (overlayIt == overlays.end()) {
            std::cerr << "Invalid overlay pointer" << std::endl;
            return env->NewStringUTF("");
        }
        
        const char* scriptStr = env->GetStringUTFChars(script, nullptr);
        RefPtr<Overlay> overlay = overlayIt->second;
        
        JSValue result = overlay->view()->EvaluateScript(scriptStr);
        String resultStr = result.ToString();
        
        env->ReleaseStringUTFChars(script, scriptStr);
        return env->NewStringUTF(resultStr.utf8().data());
    } catch (const std::exception& e) {
        std::cerr << "Error getting script result: " << e.what() << std::endl;
        return env->NewStringUTF("");
    }
}

} // extern "C"