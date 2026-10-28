package eng;

import eng.async.Async;
import eng.specialized.Specialized;
import eng.ubershader.Ubershader;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("Missing flag (-specialized, -ubershader, -async, -printgpu)");
            System.exit(-1);
        }
        switch (args[0]) {
            case "-specialized" -> new Specialized().launch("specialized", 1920, 1080);
            case "-ubershader" -> new Ubershader().launch("ubershader", 1920, 1080);
            case "-async" -> new Async().launch("async", 1920, 1080);
            case "-printgpu" -> printGLDevice();

            default -> System.err.println("Unknown flag " + args[1] + ". Try -printgpu, -specialized, -ubershader or -async");
        }
    }

    private static void printGLDevice() {
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        long window = glfwCreateWindow(1, 1, "Headless", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        String renderer = glGetString(GL_RENDERER);
        String vendor = glGetString(GL_VENDOR);
        String version = glGetString(GL_VERSION);

        System.out.println(vendor + " " + renderer + " OpenGL " + version);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
