package com.smartledger.smartledger.repository;

import com.smartledger.smartledger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
}
