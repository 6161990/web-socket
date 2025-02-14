package com.yoon.canufeelmyheartbeat.repositories;

import com.yoon.canufeelmyheartbeat.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberChatroomMappingRepository extends JpaRepository<MemberChatroomMapping, Long> {

    Boolean existsByMemberIdAndChatroomId(Long memberId, Long chatroomId);

    void deleteByMemberIdAndChatroomId(Long memberId, Long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long memberId);

    Optional<MemberChatroomMapping> findByMemberIdAndChatroomId(Long memberId, Long chatroomId);
}
