package library.Assets;

import com.jmc.library.Assets.GoogleBookInfo;
import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class GoogleBookInfoTest {

    private GoogleBookInfo googleBookInfo;
    private GoogleBookAPIMethod mockApiMethod;

    @BeforeEach
    void setUp() {
        mockApiMethod = Mockito.mock(GoogleBookAPIMethod.class);
        googleBookInfo = new GoogleBookInfo(mockApiMethod);
    }

    @Test
    void testGetInfo() {
        JSONObject fakeBookInfo = new JSONObject();
        fakeBookInfo.put("totalItems", 1);
        when(mockApiMethod.searchBook(anyString())).thenReturn(fakeBookInfo);

        googleBookInfo.setISBN("1234567890");
        googleBookInfo.getInfo();

        assertTrue(googleBookInfo.isExist());
        assertTrue(googleBookInfo.isLoaded());
    }

    @Test
    void testGetInfo_NoItems() {
        JSONObject fakeBookInfo = new JSONObject();
        fakeBookInfo.put("totalItems", 0);

        when(mockApiMethod.searchBook(anyString())).thenReturn(fakeBookInfo);

        googleBookInfo.setISBN("1234567890");
        googleBookInfo.getInfo();

        assertFalse(googleBookInfo.isExist());
        assertTrue(googleBookInfo.isLoaded());
        assertNull(googleBookInfo.getBookName());
        assertNull(googleBookInfo.getAuthorName());
        assertNull(googleBookInfo.getPublishedDate());
    }
}
