package fileUploader.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileItemTest {

    @Test
    void testFileItemStoresCorrectData() {

        File file = new File("cv.pdf");

        FileItem item = new FileItem(
                file,
                "cv.pdf",
                "pdf",
                FileCategory.DOCUMENTS,
                true
        );

        assertAll(
                () -> assertEquals(file, item.getFile()),
                () -> assertEquals("cv.pdf", item.getFileName()),
                () -> assertEquals("pdf", item.getExtension()),
                () -> assertEquals(FileCategory.DOCUMENTS, item.getCategory()),
                () -> assertTrue(item.isSelected())
        );
    }

    @Test
    void testFileItemWithUnselectedState() {

        FileItem item = new FileItem(
                new File("notes.txt"),
                "notes.txt",
                "txt",
                FileCategory.DOCUMENTS,
                false
        );

        assertFalse(item.isSelected());
    }

    @Test
    void testFileItemAcceptsNullCategory() {

        FileItem item = new FileItem(
                new File("a.txt"),
                "a.txt",
                "txt",
                null,
                false
        );

        assertNull(item.getCategory());
    }

    @Test
    void testFileItemAcceptsNullExtension() {

        FileItem item = new FileItem(
                new File("image"),
                "image",
                null,
                FileCategory.IMAGES,
                true
        );

        assertNull(item.getExtension());
    }

    @Test
    void testFileItemAcceptsNullFileName() {

        FileItem item = new FileItem(
                new File("unknown"),
                null,
                "bin",
                FileCategory.OTHER,
                false
        );

        assertNull(item.getFileName());
    }

    @Test
    void testDifferentObjectsAreIndependent() {

        FileItem item1 = new FileItem(
                new File("a.pdf"),
                "a.pdf",
                "pdf",
                FileCategory.DOCUMENTS,
                true
        );

        FileItem item2 = new FileItem(
                new File("a.pdf"),
                "a.pdf",
                "pdf",
                FileCategory.DOCUMENTS,
                true
        );

        assertNotSame(item1, item2);
    }

    @Test
    void testFileReferenceIsPreserved() {

        File originalFile = new File("report.docx");

        FileItem item = new FileItem(
                originalFile,
                "report.docx",
                "docx",
                FileCategory.DOCUMENTS,
                true
        );

        assertSame(
                originalFile,
                item.getFile()
        );
    }

    @Test
    void testFileItemHandlesEmptyExtension() {

        FileItem item = new FileItem(
                new File("README"),
                "README",
                "",
                FileCategory.OTHER,
                true
        );

        assertEquals(
                "",
                item.getExtension()
        );
    }
}