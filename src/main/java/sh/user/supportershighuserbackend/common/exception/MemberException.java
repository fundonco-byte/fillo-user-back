package sh.user.supportershighuserbackend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sh.user.supportershighuserbackend.member.request.MemberRegistRequestDto;

public interface MemberException {

    // 회원 계정 생성 확인
    String checkRegistInfo(MemberRegistRequestDto memberRegistRequestDto);

    // 회원 계정 생성 시 비밀번호 재확인
    String checkRightPassword(String password, String checkPassword);
}
