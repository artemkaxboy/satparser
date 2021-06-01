package com.artemkaxboy.satparser.tools

import org.springframework.transaction.interceptor.TransactionAspectSupport

fun rollbackTransaction() = TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
