package com.yoon.canufeelmyheartbeat.services;

import com.yoon.canufeelmyheartbeat.dtos.ChatroomDto;
import com.yoon.canufeelmyheartbeat.dtos.MemberDto;
import com.yoon.canufeelmyheartbeat.entities.Chatroom;
import com.yoon.canufeelmyheartbeat.entities.Member;
import com.yoon.canufeelmyheartbeat.enums.Role;
import com.yoon.canufeelmyheartbeat.repositories.ChatroomRepository;
import com.yoon.canufeelmyheartbeat.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultantService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username).get();

        if (Role.fromCode(member.getRole()) != Role.CONSULTANT) {
            throw new AccessDeniedException("상담사가 아닙니다.");
        }

        return new CustomUserDetails(member, null);
    }

    public MemberDto saveMember(MemberDto memberDto) {
        Member member = MemberDto.to(memberDto);
        member.updatePassword(memberDto.password(), memberDto.confirmedPassword(), passwordEncoder);

        member = memberRepository.save(member);

        return MemberDto.from(member);
    }

    public Page<ChatroomDto> getChatroomPage(Pageable pageable) {
        Page<Chatroom> chatroomPage = chatroomRepository.findAll(pageable);

        return chatroomPage.map(ChatroomDto::from);
    }
}
