#include "Arduino.h"
class Event {
public:
  Event(String command) : command(command) {}
  String getCommand() const { return command; }
private:
  String command;
};

// Node class for linked list
class Node {
public:
  Node(const Event& event) : event(event), next(nullptr) {}
  Event event;
  Node* next;
};

// Queue class for event handling
class EventQueue {
public:
  EventQueue() : head(nullptr), tail(nullptr) {}

  void enqueue(const Event& event) {
    Node* node = new Node(event);
    if (isEmpty()) {
      head = tail = node;
    } else {
      tail->next = node;
      tail = node;
    }
  }

  Event dequeue() {
    if (isEmpty()) {
      return Event(""); // Return an invalid event if queue is empty
    }
    Event event = head->event;
    Node* temp = head;
    head = head->next;
    delete temp;
    if (isEmpty()) {
      tail = nullptr;
    }
    return event;
  }

  bool isEmpty() const {
    return head == nullptr;
  }

private:
  Node* head;
  Node* tail;
};
