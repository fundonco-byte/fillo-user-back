package sh.user.supportershighuserbackend.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import sh.user.supportershighuserbackend.common.util.LogUtil;

import java.lang.reflect.Field;

@Slf4j
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper=false)
public class AbstractVO {
    protected String rowNum;    // 조회 완료 시 row number
    protected String rowCnt;    // 조회 완료 시 row count

    public String toJsonString() {
        try {
            ObjectMapper json = new ObjectMapper();
            return json.writeValueAsString(this);
        } catch (Exception ex) {
            LogUtil.logException(ex);
        }

        return null;
    }

    // 전달받은 json 형식의 텍스트(string) 데이터와 실제 json 객체로 변환할 class 객체를 가지고 매핑하여 Json 반환
    @JsonIgnore
    public <T> T JsonStringToObj(String jsonString, Class<T> cls) throws JsonProcessingException {
        ObjectMapper json = new ObjectMapper();
        return json.readValue(jsonString, cls);
    }

    // 전달받은 필드명(name)을 가지고 찾아서 반환
    @JsonIgnore
    public Object getField(String name) {
        Object obj;
        try {
            Field field = this.getClass().getDeclaredField(name);
            obj = field.get(this);
        } catch (Exception ex) {
            obj = null;
            LogUtil.logException(ex);
        }

        return obj;
    }

    
    // 전달받은 필드명(name), 값(value)를 가지고 AbstractVO에 필드 생성
    @JsonIgnore
    public void setField(String name, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.set(this, value);
        } catch (Exception e) {
            LogUtil.logException(e);
        }
    }

    // 전달받은 객체(Generic)의 모든 필드값을 복사해서 현재 AbstractVO에 전체 복사
    @JsonIgnore
    public <T extends AbstractVO> void copyAllField(T obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            String name = field.getName();
            this.setField(name, obj.getField(name));
        }
    }
}
