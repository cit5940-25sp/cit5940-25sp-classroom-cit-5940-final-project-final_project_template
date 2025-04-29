package api.responses;

import java.util.List;
import java.util.Map;

/**
 * API response base class, used for standardizing response format
 */
public class ApiResponse {
    private int code;
    private String message;
    private Object data;
    
    /**
     * Constructor
     */
    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * Create success response
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "success", data);
    }
    
    /**
     * Create success response (no data)
     */
    public static ApiResponse success() {
        return new ApiResponse(200, "success", null);
    }
    
    /**
     * Create error response
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, message, null);
    }
    
    /**
     * Convert response to JSON string
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"code\":").append(code).append(",");
        json.append("\"message\":\"").append(message).append("\",");
        json.append("\"data\":");
        
        if (data == null) {
            json.append("null");
        } else if (data instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> dataMap = (Map<String, Object>) data;
            json.append(mapToJson(dataMap));
        } else if (data instanceof List) {
            json.append(listToJson((List<?>) data));
        } else {
            json.append("\"").append(data.toString()).append("\"");
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convert Map to JSON string
     */
    private String mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value == null) {
                json.append("null");
            } else if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> valueMap = (Map<String, Object>) value;
                json.append(mapToJson(valueMap));
            } else if (value instanceof List) {
                json.append(listToJson((List<?>) value));
            } else {
                json.append("\"").append(value.toString()).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convert List to JSON string
     */
    private String listToJson(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            if (item == null) {
                json.append("null");
            } else if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> itemMap = (Map<String, Object>) item;
                json.append(mapToJson(itemMap));
            } else if (item instanceof List) {
                json.append(listToJson((List<?>) item));
            } else {
                json.append("\"").append(item.toString()).append("\"");
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    // Getters
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Object getData() {
        return data;
    }
}
