Feature: using helper java class to run karate test with rabbitmq

  Background:
    * def queue = 'worker.queue.1'

  Scenario: checking rabbit pushing and consumer
    * rmq.publish(queue, 'working with karate')
    * def result = rmq.msg()
    * match result == 'working with karate'



