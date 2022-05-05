package com.tanzdata.tanzdata.repo;

import com.tanzdata.tanzdata.models.RedditComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedditCommentsRepo extends JpaRepository<RedditComments, Long> {
    Optional<RedditComments> findFirstByParentId(String parentId);
    Optional<RedditComments> findFirstByCommentId(String parentId);
}
