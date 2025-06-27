package org.example.service;

import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class LedgerRequestReplyClient {

//   private final RabbitTemplate rabbitTemplate;
//
//   public LedgerRequestReplyClient(RabbitTemplate rabbitTemplate) {
//       this.rabbitTemplate = rabbitTemplate;
//   }
//
//   public Mono<TransactionDto> sendTransaction(TransactionDto tx) {
//       System.out.println("se fue por cash-out-reply");
//       return Mono.fromCallable(() ->
//           (TransactionDto) rabbitTemplate.convertSendAndReceive(
//               "ledger.exchange",
//               "ledger.entry.request",
//               tx
//           )
//       ).subscribeOn(Schedulers.boundedElastic());
//   }
}