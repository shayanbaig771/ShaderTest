
import static org.lwjgl.opengl.GL46.*;

public class VertexBuffer {
    public int myVbo;
    public int myEbo;

    private int maxVertices;
    private int numOfVertices;
    private int vertexDataSize;

    public VertexBuffer(int maxVertices, int vertexDataSize) {
        this.vertexDataSize = vertexDataSize;
        this.maxVertices = maxVertices;
        build();
    }

    public int getNumOfVertices() {
        return numOfVertices;
    }

    public int getVertexDataSize() {
        return vertexDataSize;
    }


    /***
     * Actually allocates space for the Vertex Buffer and Index Buffer
     */
    private void build() {
        myVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, myVbo);
        glBufferData(GL_ARRAY_BUFFER, maxVertices * vertexDataSize * Float.BYTES, GL_DYNAMIC_DRAW);
        numOfVertices = maxVertices;

        myEbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, myEbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (maxVertices / 4) * 6L * Integer.BYTES, GL_DYNAMIC_DRAW);

    }
    public void dispose() {
        glDeleteBuffers(myVbo);
        glDeleteBuffers(myEbo);
    }
}