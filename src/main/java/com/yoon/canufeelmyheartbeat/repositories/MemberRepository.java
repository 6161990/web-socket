package com.yoon.canufeelmyheartbeat.repositories;

import java.util.Optional;

import com.yoon.canufeelmyheartbeat.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);
}
