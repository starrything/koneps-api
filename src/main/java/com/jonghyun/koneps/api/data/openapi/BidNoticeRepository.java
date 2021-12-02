package com.jonghyun.koneps.api.data.openapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidNoticeRepository extends JpaRepository<BidNotice, BidNoticeKey> {
}
