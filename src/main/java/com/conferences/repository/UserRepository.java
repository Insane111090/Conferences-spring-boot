package com.conferences.repository;

import com.conferences.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
      extends JpaRepository<User, Long>{
    
    Optional<User> findOneByEmailAndPasswordIgnoreCase(final String email, final String password);
    
    Optional<User> findOneByEmailIgnoreCase(final String email);
    
    @Query(value = "select u.*\n" +
                   "from CONFERENCES c,\n" +
                   "     USERS_CONFERENCES uc,\n" +
                   "     users u\n" +
                   "where c.CONFERENCE_ID = uc.CONFERENCE_ID\n" +
                   "and uc.USER_ID = u.USER_ID\n" +
                   "and c.CONFERENCE_ID=:conferenceId", nativeQuery = true)
    List<User> findParticipants(@Param("conferenceId") long conferenceId);
    
/*    @Query(value = "select u.*, uc.role\n" +
                   "from CONFERENCES c,\n" +
                   "     USERS_CONFERENCES uc,\n" +
                   "     users u\n" +
                   "where c.CONFERENCE_ID = uc.CONFERENCE_ID\n" +
                   "and uc.USER_ID = u.USER_ID\n" +
                   "and c.CONFERENCE_ID=:conferenceId", nativeQuery = true)
    Participant findParticipant(@Param("conferenceId") long conferenceId);*/
    
}
