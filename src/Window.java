import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long window;
    private String glslVersion;
    public Window(String title, int width, int height) {
        //init
        {
            GLFWErrorCallback.createPrint(System.err).set();
            if ( !glfwInit() )
                throw new IllegalStateException("Unable to initialize GLFW");

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            {
                final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
                if (isMac) {
                    glslVersion = "#version 150";
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
                    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
                    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);          // Required on Mac
                } else {
                    glslVersion = "#version 130";
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
                }
            }

            window = glfwCreateWindow(width, height , title, NULL, NULL);
            if ( window == NULL )
                throw new RuntimeException("Failed to create the GLFW window");



            try ( MemoryStack stack = stackPush() ) {
                IntBuffer pWidth = stack.mallocInt(1); // int*
                IntBuffer pHeight = stack.mallocInt(1); // int*

                glfwGetWindowSize(window, pWidth, pHeight);

                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


                glfwSetWindowPos(
                        window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }
            glfwMakeContextCurrent(window);
            GL.createCapabilities();
            glfwSwapInterval(1);
            glfwShowWindow(window);


        }

    }

    public double getTime() {
        return glfwGetTime();
    }
    public String getGlslVersion() {
        return glslVersion;
    }

    public long getWindow() {
        return window;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }
    public void tick() {


        glfwSwapBuffers(window);
        glfwPollEvents();
    }
    public void dispose() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
