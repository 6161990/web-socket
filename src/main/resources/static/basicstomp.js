const stompClient = new StompJs.Client({
  brokerURL: 'ws://localhost:8080/stomp/heartbeat/chats'
});

stompClient.onConnect = (frame) => {
  setConnected(true);
  console.log('Connected: ' + frame);
  stompClient.subscribe('/sub/chats',
      (chatMessage) => {
        showMessage(JSON.parse(chatMessage.body));
  });
  stompClient.publish({
    destination: "/pub/chats",
    body: JSON.stringify(
        {'message': 'connected'})
  });
};


stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

function setConnected(connected){
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if(connected){
    $("#conversation").show();
  }else{
    $("#conversation").hide();
  }
  $("#messages").html("");
}

function connect() {
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  setConnected(false);
  console.log("Disconnected");
}

function sendMessage() {
  stompClient.publish({
    destination: "/pub/chats",
    body: JSON.stringify(
        {'message': $("#message").val()})
  });
  $("#message").val("")
}

function showMessage(chatMessage) {
  $("#messages").append(
      "<tr><td>" + chatMessage.sender + " : " + chatMessage.message
      + "</td></tr>");
}

$(function () {
  $("form").on('submit', (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendMessage());
});
