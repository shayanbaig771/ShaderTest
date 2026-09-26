
import static org.lwjgl.opengl.GL46.*;

public class VertexBuffer {
    public int myVbo;
    public int myEbo;

    private int maxVertices;
    private int maxIndices;
    private int vertexDataSize;

    public VertexBuffer(int maxVertices, int maxIndices, int vertexDataSize) {
        this.maxVertices = maxVertices;
        this.maxIndices = maxIndices;
        this.vertexDataSize = vertexDataSize;
        build();
    }


    /***
     * Actually allocates space for the Vertex Buffer and Index Buffer
     */
    private void build() {
        myVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, myVbo);
        glBufferData(GL_ARRAY_BUFFER, maxVertices * vertexDataSize * Float.BYTES, GL_DYNAMIC_DRAW);

        myEbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, myEbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, maxIndices * Integer.SIZE, GL_DYNAMIC_DRAW);

    }
    public void dispose() {
        glDeleteBuffers(myVbo);
        glDeleteBuffers(myEbo);
    }
}