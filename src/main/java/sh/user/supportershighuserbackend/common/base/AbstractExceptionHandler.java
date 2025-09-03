package sh.user.supportershighuserbackend.common.base;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;
import sh.user.supportershighuserbackend.common.exception.MemberException;
import sh.user.supportershighuserbackend.member.repository.MemberRepository;
import sh.user.supportershighuserbackend.member.request.MemberRegistRequestDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public abstract class AbstractExceptionHandler implements MemberException {

    // 모든 API의 요청 파라미터를 Valid로 1차 확인 후 에러가 발생 시 우선적으로 선행되는 ExceptionHandler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 회원 계정 생성 확인
    @Override
    public String checkRegistInfo(MemberRegistRequestDto memberRegistRequestDto) {
        if (memberRegistRequestDto.getEmail().isEmpty() || memberRegistRequestDto.getPassword().isEmpty()
        || memberRegistRequestDto.getCheckPassword().isEmpty() || memberRegistRequestDto.getName().isEmpty()
        || memberRegistRequestDto.getGender().isEmpty() || memberRegistRequestDto.getBirthDate().isEmpty()) {
            return "회원가입 정보가 옳바르지 않습니다.";
        }

        return null;
    }

    // 회원 계정 비밀번호 재확인
    @Override
    public String checkRightPassword(String password, String checkPassword){
        if(!password.equals(checkPassword)){
            return "비밀번호와 재확인 비밀번호가 일치하지 않습니다.";
        }

        return null;
    }
}
