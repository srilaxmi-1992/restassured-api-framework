package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import models.Booking;

public class TestDataLoader {

    private static final String TEST_DATA_PATH =
            "src/test/resources/testdata/booking_test_data.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode root;

    // getting testdata file in a json mapped to jsonNode
    static {
        try {
            FileInputStream fileInputStream = new FileInputStream(TEST_DATA_PATH);
            root = mapper.readTree(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException("Test data load failed → " + e.getMessage());
        }
    }

    // mapping to POJO class
    public static Booking getInput(String tcId) throws Exception {
        return mapper.treeToValue(root.path(tcId).path("input"), Booking.class);
    }

    public static Booking getUpdatedInput(String tcId) throws Exception {
        return mapper.treeToValue(root.path(tcId).path("updatedInput"), Booking.class);
    }

    // Returns expected block as JsonNode — access any field cleanly
    public static JsonNode getExpected(String tcId) {
        return root.path(tcId).path("expected");
    }
}

