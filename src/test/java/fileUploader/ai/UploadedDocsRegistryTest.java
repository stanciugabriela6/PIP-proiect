package fileUploader.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UploadedDocsRegistryTest {

    @BeforeEach
    void clearRegistry() {

        UploadedDocsRegistry.clear();
    }

    @Test
    void testAddDocument() {

        UploadedDocsRegistry.add("document1.pdf");

        List<String> docs = UploadedDocsRegistry.list();

        assertTrue(
                docs.contains("document1.pdf")
        );
    }

    @Test
    void testRemoveDocument() {

        UploadedDocsRegistry.add("document1.pdf");

        UploadedDocsRegistry.remove("document1.pdf");

        List<String> docs = UploadedDocsRegistry.list();

        assertFalse(
                docs.contains("document1.pdf")
        );
    }

    @Test
    void testClearRegistry() {

        UploadedDocsRegistry.add("doc1.pdf");
        UploadedDocsRegistry.add("doc2.pdf");

        UploadedDocsRegistry.clear();

        assertTrue(
                UploadedDocsRegistry.list().isEmpty()
        );
    }

    @Test
    void testListReturnsCorrectSize() {

        UploadedDocsRegistry.add("a.pdf");
        UploadedDocsRegistry.add("b.pdf");

        assertEquals(
                2,
                UploadedDocsRegistry.list().size()
        );
    }

    @Test
    void testDuplicateDocumentsAreIgnored() {

        UploadedDocsRegistry.add("same.pdf");
        UploadedDocsRegistry.add("same.pdf");

        assertEquals(
                1,
                UploadedDocsRegistry.list().size()
        );
    }

    @Test
    void testNullDocumentIsIgnored() {

        UploadedDocsRegistry.add(null);

        assertTrue(
                UploadedDocsRegistry.list().isEmpty()
        );
    }

    @Test
    void testBlankDocumentIsIgnored() {

        UploadedDocsRegistry.add("   ");

        assertTrue(
                UploadedDocsRegistry.list().isEmpty()
        );
    }

    @Test
    void testRemoveNonExistingDocument() {

        UploadedDocsRegistry.remove("missing.pdf");

        assertTrue(
                UploadedDocsRegistry.list().isEmpty()
        );
    }

    @Test
    void testDocumentsMaintainInsertionOrder() {

        UploadedDocsRegistry.add("first.pdf");
        UploadedDocsRegistry.add("second.pdf");

        List<String> docs = UploadedDocsRegistry.list();

        assertEquals(
                "first.pdf",
                docs.get(0)
        );

        assertEquals(
                "second.pdf",
                docs.get(1)
        );
    }

    @Test
    void testListReturnsNewCollection() {

        UploadedDocsRegistry.add("doc.pdf");

        List<String> docs = UploadedDocsRegistry.list();

        docs.clear();

        assertEquals(
                1,
                UploadedDocsRegistry.list().size()
        );
    }

    @Test
    void testAddMultipleDocuments() {

        UploadedDocsRegistry.add("1.pdf");
        UploadedDocsRegistry.add("2.pdf");
        UploadedDocsRegistry.add("3.pdf");

        assertEquals(
                3,
                UploadedDocsRegistry.list().size()
        );
    }

    @Test
    void testRegistryInitiallyEmptyAfterClear() {

        UploadedDocsRegistry.clear();

        assertTrue(
                UploadedDocsRegistry.list().isEmpty()
        );
    }
}