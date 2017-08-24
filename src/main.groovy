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

glfwWindowHint(GLFW_STENCIL_BITS, 8);
glfwWindowHint(GLFW_SAMPLES, 8);
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

double lastTime = glfwGetTime();
int nbFrames = 0;

// Run the rendering loop until the user has attempted to close
// the window or has pressed the ESCAPE key.
while (!glfwWindowShouldClose(window)) {

    double currentTime = glfwGetTime();
    nbFrames++;
    if (currentTime - lastTime >= 1.0) {
        println("FPS: " + (1f * nbFrames / (currentTime - lastTime)))
        nbFrames = 0;
        lastTime += 1.0;
    }

    // Viewport
    glViewport(0, 0, (int) width, (int) height);

    // Perspective
    m.setPerspective((float) Math.toRadians(100f), (float) (width / height), 0.01f, 10000.0f);
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixf(m.get(fb));

    // Camera position
    m.setLookAt(4.0f, 5.5f, 18.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f);
    glMatrixMode(GL_MODELVIEW);
    glLoadMatrixf(m.get(fb));

    // Drawing
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

    for (int i = -10; i < 50; i++) {
        glPushMatrix()

        glTranslatef(0.0f, 0.0f, (float) (i * -10.0f));

        if (i == 0) {
            glRotatef(rtri, 0.0f, 1.0f, 0.0f);
        }

        glBegin(GL_TRIANGLES);

        float[] red = [1.0f, 0.0f, 0.0f]
        float[] green = [0.0f, 1.0f, 0.0f]
        float[] blue = [0.0f, 0.0f, 1.0f]

        float[] top = [0.0f, 1.0f, 0.0f]
        float[] frontLeft = [-1.0f, -1.0f, 1.0f]
        float[] frontRight = [1.0f, -1.0f, 1.0f]
        float[] backLeft = [-1.0f, -1.0f, -1.0f]
        float[] backRight = [1.0f, -1.0f, -1.0f]


        glColor3fv(red)
        glVertex3fv(top)
        glColor3fv(green)
        glVertex3fv(frontLeft)
        glColor3fv(blue)
        glVertex3fv(frontRight)

        glColor3fv(red)
        glVertex3fv(top)
        glColor3fv(blue)
        glVertex3fv(frontRight)
        glColor3fv(green)
        glVertex3fv(backRight)

        glColor3fv(red)
        glVertex3fv(top)
        glColor3fv(green)
        glVertex3fv(backRight)
        glColor3fv(blue)
        glVertex3fv(backLeft)

        glColor3fv(red)
        glVertex3fv(top)
        glColor3fv(blue)
        glVertex3fv(backLeft)
        glColor3fv(green)
        glVertex3fv(frontLeft)

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
