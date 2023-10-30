package com.pa.modules.committee.repository;

import com.pa.modules.committee.consts.ConstCommittee;
import com.pa.modules.committee.model.Committee;
import com.pa.modules.committee.model.MembershipRequest;
import com.pa.modules.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {


    MembershipRequest findByCommitteeAndUser(Committee c, Users u);

    List<MembershipRequest> findByUser(Users u);

    @Query(nativeQuery = true, value = " SELECT  * FROM  membership_request mr WHERE   mr.user_id = :user " +
            " and ( status=" + ConstCommittee.COMMITTEE_STATUS_DRAFT + " OR status= " + ConstCommittee.COMMITTEE_STATUS_ACCEPT + ")")
    List<MembershipRequest> findNonAcceptedMembershipRequestByUser(@Param("user") Long user);

    @Query(nativeQuery = true, value = "" +
            " (SELECT  * FROM  membership_request mr WHERE   mr.user_id = :user )" +
            " UNION ALL " +
            " (  SELECT  * FROM  membership_request mr WHERE 1=1 " +
            //"  status =  "+ ConstCommittee.COMMITTEE_STATUS_DRAFT +
            " AND exists (  SELECT committee_id FROM committee_membership cm WHERE committee_id = ( SELECT parent_id FROM committee WHERE id=mr.committee_id)  AND cm.user_id = :user )" +
            " )")
    List<MembershipRequest> getAllMembershipRequestForOwner(@Param("user") Long user);

    @Query(nativeQuery = true, value = " SELECT  * FROM  membership_request mr WHERE   mr.user_id = :user  ")
    List<MembershipRequest> getAllMembershipRequestFor(@Param("user") Long user);


    @Modifying
    @Query(nativeQuery = true, value = " INSERT INTO committee_membership ( user_id , committee_id ) VALUES ( :user, :committee) ")
    void acceptMembershipRequest(@Param("user") Long user, @Param("committee") Long committee);


}
