package sh.user.supportershighuserbackend.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    // 이메일 인증 값 메일 전송
    // 우선, 인증값을 생성 후 목적지 이메일로 전송 후, 인증값을 프론트로 반환하여 해당 프론트 단에서 인증값을 가지고 비교하여 회원가입을 진행
    public String sendAuthorizeEmail(String email) {

        try {
            // 단순 문자 메일을 보낼 수 있는 객체 생성
            SimpleMailMessage message = new SimpleMailMessage();

            // 이메일 보안 인증번호 추출
            SecureRandom secureRandom = new SecureRandom();
            int number = 100000 + secureRandom.nextInt(900000);
            String sixDigit = String.format("%06d", number);

            message.setTo(email); // 메일을 받을 목적지 이메일
            message.setSubject("[Fillo] 이메일 인증 확인"); // 메일 제목
            message.setText(
                            "계정 인증을 위한 이메일 주소 인증번호입니다. \n" +
                            "인증화면에 아래 인증 번호를 입력해주세요. \n" +
                            sixDigit
            ); // 메일 내용

            // 메일 전송
            javaMailSender.send(message);

            return sixDigit;
        } catch (MailSendException m) {
            m.printStackTrace();
            return null;
        }
    }


    // 임시 비밀번호 재발급 메일 보내기
//    public void sendPasswordEmail(Member loginMember, String immediatePassword) {
//
//        try {
//            // 단순 문자 메일을 보낼 수 있는 객체 생성
//            SimpleMailMessage message = new SimpleMailMessage();
//
//            message.setTo(loginMember.getEmail()); // 메일을 받을 목적지 이메일
//            //message.setTo("wlstpgns51@naver.com"); // 메일을 받을 목적지 이메일
//
//            message.setSubject("[확인] 온누리 몰 계정 임시 비밀번호 발급"); // 메일 제목
//
//            if (loginMember.getType().equals("C")) {
//                // 메일 내용
//                message.setText(
//                        loginMember.getUserName() + " 님의 비밀번호는 " + immediatePassword + " 입니다."
//                );
//            } else if (loginMember.getType().equals("B")) {
//                // 메일 내용
//                message.setText(
//                        loginMember.getManager() + " 님의 비밀번호는 " + immediatePassword + " 입니다."
//                );
//            }
//
//            // 메일 전송
//            javaMailSender.send(message);
//        } catch (MailSendException m) {
//            m.printStackTrace();
//        }
//    }


    // HTML 메일 보내기
    public void sendHTMLEmail() {

    }

    // 6자리 랜덤 비밀번호 생성
    public void createRandomPw() {

    }
}
