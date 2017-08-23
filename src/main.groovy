@Grab(group = 'org.lwjgl', module = 'lwjgl', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl-glfw', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl-opengl', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl-openal', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl-jemalloc', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl-stb', version = '3.1.2')
@Grab(group = 'org.lwjgl', module = 'lwjgl', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.lwjgl', module = 'lwjgl-glfw', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl-glfw', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl-glfw', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.lwjgl', module = 'lwjgl-opengl', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl-opengl', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl-opengl', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.lwjgl', module = 'lwjgl-openal', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl-openal', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl-openal', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.lwjgl', module = 'lwjgl-jemalloc', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl-jemalloc', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl-jemalloc', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.lwjgl', module = 'lwjgl-stb', version = '3.1.2', classifier = 'natives-windows')
@Grab(group = 'org.lwjgl', module = 'lwjgl-stb', version = '3.1.2', classifier = 'natives-linux')
@Grab(group = 'org.lwjgl', module = 'lwjgl-stb', version = '3.1.2', classifier = 'natives-macos')
@Grab(group = 'org.joml', module = 'joml', version = '1.9.4')

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack

import java.nio.FloatBuffer
import java.nio.IntBuffer

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import static org.lwjgl.glfw.GLFW.*
import static org.lwjgl.opengl.GL11.*
import static org.lwjgl.system.MemoryStack.stackPush
import static org.lwjgl.system.MemoryUtil.NULL

float width = 1024
float height = 768

GLFWErrorCallback.createPrint(System.err).set()

if (!glfwInit())
    throw new IllegalStateException("Unable to initialize GLFW")


glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

// Create window
window = glfwCreateWindow((int) width, (int) height, "Hello World!", NULL, NULL);
if (window == NULL)
    throw new RuntimeException("Failed to create the GLFW window");

// ESC - exit
glfwSetKeyCallback(window, new GLFWKeyCallback() {
    @Override
    void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    }
});

// Center window
MemoryStack stack = stackPush()
try {
    IntBuffer pWidth = stack.mallocInt(1); // int*
    IntBuffer pHeight = stack.mallocInt(1); // int*

    // Get the window size passed to glfwCreateWindow
    glfwGetWindowSize(window, pWidth, pHeight);

    // Get the resolution of the primary monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

    // Center the window
    glfwSetWindowPos(
            window,
            (int) ((vidmode.width() - pWidth.get(0)) / 2),
            (int) ((vidmode.height() - pHeight.get(0)) / 2)
    );
} finally {
    stack.close()
}

// Make the OpenGL context current
glfwMakeContextCurrent(window);
// Enable v-sync
glfwSwapInterval(1);

// Make the window visible
glfwShowWindow(window);


GL.createCapabilities();

// INIT
glShadeModel(GL_SMOOTH);

// Set the clear color
glClearColor(0.3f, 0.3f, 0.3f, 0.0f);

glClearDepth(1.0f);                         // Depth Buffer Setup
glEnable(GL_DEPTH_TEST);                        // Enables Depth Testing
glDepthFunc(GL_LEQUAL);                         // The Type Of Depth Test To Do

glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);          // Really Nice Perspective Calculations

FloatBuffer fb = BufferUtils.createFloatBuffer(16);
Matrix4f m = new Matrix4f();

float rtri = 0

// Run the rendering loop until the user has attempted to close
// the window or has pressed the ESCAPE key.
while (!glfwWindowShouldClose(window)) {
    glViewport(0, 0, (int) width, (int) height);


    m.setPerspective((float) Math.toRadians(40.0f), (float) (width / height), 0.01f, 10000.0f);
    glMatrixMode(GL_PROJECTION);
//    glLoadIdentity();
    glLoadMatrixf(m.get(fb));

    m.setLookAt(4.0f, 5.5f, 18.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f);
    glMatrixMode(GL_MODELVIEW);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    glLoadMatrixf(m.get(fb));

    for (int i = -10; i < 50; i++) {
        glPushMatrix()

        glTranslatef(0.0f, 0.0f, (float) (i * -10.0f));

        if (i == 0) {
            glRotatef(rtri, 0.0f, 1.0f, 0.0f);
        }
        
        glBegin(GL_TRIANGLES);

        glColor3f(1.0f, 0.0f, 0.0f);          // Red
        glVertex3f(0.0f, 1.0f, 0.0f);          // Top Of Triangle (Front)
        glColor3f(0.0f, 1.0f, 0.0f);          // Green
        glVertex3f(-1.0f, -1.0f, 1.0f);          // Left Of Triangle (Front)
        glColor3f(0.0f, 0.0f, 1.0f);          // Blue
        glVertex3f(1.0f, -1.0f, 1.0f);          // Right Of Triangle (Front)

        glColor3f(1.0f, 0.0f, 0.0f);          // Red
        glVertex3f(0.0f, 1.0f, 0.0f);          // Top Of Triangle (Right)
        glColor3f(0.0f, 0.0f, 1.0f);          // Blue
        glVertex3f(1.0f, -1.0f, 1.0f);          // Left Of Triangle (Right)
        glColor3f(0.0f, 1.0f, 0.0f);          // Green
        glVertex3f(1.0f, -1.0f, -1.0f);         // Right Of Triangle (Right)

        glColor3f(1.0f, 0.0f, 0.0f);          // Red
        glVertex3f(0.0f, 1.0f, 0.0f);          // Top Of Triangle (Back)
        glColor3f(0.0f, 1.0f, 0.0f);          // Green
        glVertex3f(1.0f, -1.0f, -1.0f);         // Left Of Triangle (Back)
        glColor3f(0.0f, 0.0f, 1.0f);          // Blue
        glVertex3f(-1.0f, -1.0f, -1.0f);         // Right Of Triangle (Back)

        glColor3f(1.0f, 0.0f, 0.0f);          // Red
        glVertex3f(0.0f, 1.0f, 0.0f);          // Top Of Triangle (Left)
        glColor3f(0.0f, 0.0f, 1.0f);          // Blue
        glVertex3f(-1.0f, -1.0f, -1.0f);          // Left Of Triangle (Left)
        glColor3f(0.0f, 1.0f, 0.0f);          // Green
        glVertex3f(-1.0f, -1.0f, 1.0f);          // Right Of Triangle (Left)

        glEnd();                        // Done Drawing The Pyramid

        glPopMatrix()
    }

    glfwSwapBuffers(window); // swap the color buffers

    // Poll for window events. The key callback above will only be
    // invoked during this call.
    glfwPollEvents();
    glFlush();

    rtri += 0.7f;
}

// Free the window callbacks and destroy the window
glfwFreeCallbacks(window);
glfwDestroyWindow(window);

// Terminate GLFW and free the error callback
glfwTerminate();
glfwSetErrorCallback(null).free();
