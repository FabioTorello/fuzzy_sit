#include "ros/ros.h"
#include "std_msgs/String.h"

#include <sstream>
//Include the messages in the folder msgs
/*#include <fuzzy_sit_memory_msgs/Scene.h>
#include <fuzzy_sit_memory_msgs/FuzzyDegree.h>
#include <fuzzy_sit_memory_msgs/FuzzyDegree.h>
#include <fuzzy_sit_memory_msgs/SceneItem.h>*/
#include <fuzzy_sit_memory_msgs/TestMessageRes.h>
//The header file generated from the srv file where there is the service
#include <fuzzy_sit_memory_pkgs/TestServiceDirective.h>
#include <vision/Configuration.h>
#include <vision/SceneTable.h>

ros::ServiceClient *clientPtr; //pointer for a client (The client is instantiated in the main function, but it needs to be used in the callback function. I made a ros::ServiceClient pointer at a global level (w.r.t. the node) and then gave it the address of the client. In the callback function I dereference the client pointer and use it to request a service.)
int i=0; //counter for the number of gamma_i

void SUBSCRIBE_CALLBACK_FUNCTION (const vision::SceneTable::ConstPtr& msg)
{  
      //Understand the number of elements of the array scene coming from the message on the topic "scene_data"
      int size = sizeof(msg->scene)/sizeof(msg->scene[0]);
      
     /* i++;
      std::string name = "g";
      memory_pkg::MemoryService srv;
      srv.test_request.items.gamma_i = name + std::to_string(i);  //just some variables to give the service some structure.
      srv.request.someFloat = 4;
      srv.request.someFloat = 30;

      ros::ServiceClient client = (ros::ServiceClient)*clientPtr; //dereference the clientPtr

      if(client.call(srv)) //request service from the client
      {
          ROS_INFO("Success = %d", srv.response.success);
      }   
      else
      {
          ROS_ERROR("Failed to call service from motor_control_node");    
      }
*/
}

int main(int argc, char **argv)
{
    ros::init(argc, argv, "Bridge_node");
    ros::NodeHandle n;

    ros::ServiceClient client = n.serviceClient<memory_pkg::MemoryService>("memory_service"); //create the client

    clientPtr = &client; //give the address of the client to the clientPtr

    ros::Subscriber sub = n.subscribe<vision::SceneTable>("scene_data", 1000, SUBSCRIBE_CALLBACK_FUNCTION); //subscribing

    ros::spin();
    return 0;
}
