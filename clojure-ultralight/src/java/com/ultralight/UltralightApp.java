package com.ultralight;

/**
 * Java JNI wrapper for Ultralight SDK
 * Provides a bridge between Clojure and the native Ultralight C++ library
 */
public class UltralightApp {
    
    static {
        try {
            // Load the native library
            System.loadLibrary("ultralight-jni");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load ultralight-jni library: " + e.getMessage());
            throw e;
        }
    }
    
    // Native method declarations
    private native long createApp();
    private native long createWindow(long appPtr, int width, int height, String title);
    private native long createOverlay(long windowPtr, int width, int height);
    private native void loadHTML(long overlayPtr, String html);
    private native void loadURL(long overlayPtr, String url);
    private native void runApp(long appPtr);
    private native void destroyApp(long appPtr);
    private native void setWindowTitle(long windowPtr, String title);
    private native void evaluateScript(long overlayPtr, String script);
    private native String getScriptResult(long overlayPtr, String script);
    
    // Instance variables to hold native pointers
    private long appPtr = 0;
    private long windowPtr = 0;
    private long overlayPtr = 0;
    
    /**
     * Initialize the Ultralight application
     */
    public void initialize(int width, int height, String title) {
        appPtr = createApp();
        if (appPtr == 0) {
            throw new RuntimeException("Failed to create Ultralight app");
        }
        
        windowPtr = createWindow(appPtr, width, height, title);
        if (windowPtr == 0) {
            throw new RuntimeException("Failed to create window");
        }
        
        overlayPtr = createOverlay(windowPtr, width, height);
        if (overlayPtr == 0) {
            throw new RuntimeException("Failed to create overlay");
        }
    }
    
    /**
     * Load HTML content into the view
     */
    public void loadHTMLContent(String html) {
        if (overlayPtr == 0) {
            throw new IllegalStateException("App not initialized");
        }
        loadHTML(overlayPtr, html);
    }
    
    /**
     * Load a URL into the view
     */
    public void loadURLContent(String url) {
        if (overlayPtr == 0) {
            throw new IllegalStateException("App not initialized");
        }
        loadURL(overlayPtr, url);
    }
    
    /**
     * Set the window title
     */
    public void setTitle(String title) {
        if (windowPtr == 0) {
            throw new IllegalStateException("Window not initialized");
        }
        setWindowTitle(windowPtr, title);
    }
    
    /**
     * Execute JavaScript in the view
     */
    public void executeScript(String script) {
        if (overlayPtr == 0) {
            throw new IllegalStateException("App not initialized");
        }
        evaluateScript(overlayPtr, script);
    }
    
    /**
     * Execute JavaScript and get the result
     */
    public String executeScriptWithResult(String script) {
        if (overlayPtr == 0) {
            throw new IllegalStateException("App not initialized");
        }
        return getScriptResult(overlayPtr, script);
    }
    
    /**
     * Run the application main loop
     */
    public void run() {
        if (appPtr == 0) {
            throw new IllegalStateException("App not initialized");
        }
        runApp(appPtr);
    }
    
    /**
     * Clean up resources
     */
    public void destroy() {
        if (appPtr != 0) {
            destroyApp(appPtr);
            appPtr = 0;
            windowPtr = 0;
            overlayPtr = 0;
        }
    }
    
    /**
     * Finalize method to ensure cleanup
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }
}