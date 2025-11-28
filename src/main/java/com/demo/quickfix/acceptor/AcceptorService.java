package com.demo.quickfix.acceptor;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.NewOrderSingle;

@Slf4j
@Service
public class AcceptorService extends MessageCracker implements Application {

  @Override
  public void onCreate(SessionID sessionID) {
    // Executed when QuickFIX/J creates a new session
    log.info("Session created {}", sessionID);
  }

  @Override
  public void onLogon(SessionID sessionID) {
    // Func gets executed when counterparty successfully login
    log.info("Logon successful {}", sessionID);
  }

  @Override
  public void onLogout(SessionID sessionID) {
    // Notifies when a FIX session goes offline
    log.info("Logout successful {}", sessionID);
  }

  @Override
  public void toAdmin(Message message, SessionID sessionID) {
    // Handles admin messages sent to counterparty
    log.info("To admin {}", sessionID);
  }

  @Override
  public void fromAdmin(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    // Handles admin message sent from counterparty
    log.info("From admin {}", sessionID);
  }

  @Override
  public void toApp(Message message, SessionID sessionID) throws DoNotSend {
    // Callback func that executes right before sending an app-level message to counterparty
    // IMPORTANT: App-level and admin-level messages are different
    log.info("Sending Message {}", sessionID);
  }

  @Override
  public void fromApp(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    // Accepts app-level messages from counterparty
    crack(message, sessionID);
  }

  @quickfix.MessageCracker.Handler
  public void onMessage(NewOrderSingle newOrderSingle, SessionID sessionID) throws FieldNotFound {
    // This func will be called whenever a NewOrderSingle is sent from the counterparty
    // IMPORTANT: To receive messages from FIX Initiator, handlers must be used
    log.info("Received NewOrderSingle {}", newOrderSingle);

    String clientOrderID = newOrderSingle.getClOrdID().getValue();
    char side = newOrderSingle.getSide().getValue();
    double orderQty = newOrderSingle.getOrderQty().getValue();
    String symbol = newOrderSingle.getSymbol().getValue();

    ExecutionReport executionReport =
        new ExecutionReport(
            new OrderID("ORDER-" + System.currentTimeMillis()),
            new ExecID("EXEC-" + System.currentTimeMillis()),
            new ExecTransType(ExecTransType.NEW),
            new ExecType(ExecType.NEW),
            new OrdStatus(OrdStatus.NEW),
            new Symbol(symbol),
            new Side(side),
            new LeavesQty(orderQty),
            new CumQty(0),
            new AvgPx(0));

    executionReport.set(new ClOrdID(clientOrderID));
    executionReport.set(new TransactTime(LocalDateTime.now()));

    try {
      Session.sendToTarget(executionReport, sessionID);
      log.info(
          "Sent ExecutionReport ({}) to [{}]: {}",
          executionReport.getExecType(),
          sessionID.getTargetCompID(),
          executionReport);
    } catch (SessionNotFound e) {
      String errMsg =
          String.format("Unable to send executionReport to [%s]", sessionID.getTargetCompID());
      throw new RuntimeException(errMsg, e);
    }
  }
}
