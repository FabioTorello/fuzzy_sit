#include "ros/ros.h"
#include "std_msgs/String.h"

#include <sstream>
//Include the messages in the folder msgs
#include <fuzzy_sit_memory_msgs/Scene.h>
#include <fuzzy_sit_memory_msgs/FuzzyDegree.h>
#include <fuzzy_sit_memory_msgs/SceneItem.h>
//#include <fuzzy_sit_memory_msgs/TestMessageRes.h>
//The header file generated from the srv file where there is the service
#include <fuzzy_sit_memory_msgs/TestServiceDirective.h>
#include <vision/Configuration.h>
#include <vision/SceneTable.h>


ros::ServiceClient *clientPtr; //pointer for a client (The client is instantiated in the main function, but it needs to be used in the callback function. I made a ros::ServiceClient pointer at a global level (w.r.t. the node) and then gave it the address of the client. In the callback function I dereference the client pointer and use it to request a service.)
int i=0; //counter for the number of gamma_i

void SUBSCRIBE_CALLBACK_FUNCTION (const vision::SceneTable::ConstPtr& msg)
{  
      //cout<<msg.c_str();
      //Understand the number of elements of the array scene coming from the message on the topic "scene_data"
      //int size = sizeof(msg->scene)/sizeof(msg->scene[0]); 
      int size= msg->scene.size();  
      i++;
      std::string name = "g";
      fuzzy_sit_memory_msgs::TestServiceDirective srv;
      //fuzzy_sit_memory_msgs::TestServiceDirectiveRequest directiveRequest; 
      //vision::SceneTable sceneTable;
      //vision::Configuration configuration;
      //message to send to the service (it is a vector of items)
      //fuzzy_sit_memory_msgs::Scene itemsInAScene;
      for (int j=0;j<size;j++){
      srv.request.test_request.items[j].gamma_i=name + std::to_string(i);
      std::cout<<srv.request.test_request.items[j];
      std::cout<<j;
      //srv.request.test_request.items[j].degrees[j].value=sceneTable.scene[j].leg_id;
      /*srv.request.test_request.items[j+1].degrees[j].value=msg->scene[j].name_config;
      srv.request.test_request.items[j+1].degrees[j].degree=0.9;
      srv.request.test_request.items[j+1].degrees[j+1].value=std::to_string(msg->scene[j].pin);
      srv.request.test_request.items[j+1].degrees[j+1].degree=0.9;*/
      /*directiveRequest.test_request.items[j].gamma_i= name + std::to_string(i);  //just some variables to give the service some structure.
      //directiveRequest.test_request.items[j].degrees[j].value=sceneTable.scene[j].leg_id; //Forse non importante
      //directiveRequest.test_request.items[j].degrees[j].value=sceneTable.scene[j].name_config;
      directiveRequest.test_request.items[j].degrees[j].value=msg->scene[j].name_config;
      directiveRequest.test_request.items[j].degrees[j].degree=0.9;
      //directiveRequest.test_request.items[j].degrees[j+1].value=std::to_string(sceneTable.scene[j].pin);
      directiveRequest.test_request.items[j].degrees[j+1].value=std::to_string(msg->scene[j].pin);
      directiveRequest.test_request.items[j].degrees[j+1].degree=0.9;  */
    
      }
     
      ros::ServiceClient client = (ros::ServiceClient)*clientPtr; //dereference the clientPtr

      if(client.call(srv)) //request service from the client
      {
          ROS_INFO("Success");
      }   
      else
      {
          ROS_ERROR("Error");    
      }

}

int main(int argc, char **argv)
{
    ros::init(argc, argv, "bridge_node");
    ros::NodeHandle n;

    ros::ServiceClient client = n.serviceClient<fuzzy_sit_memory_msgs::TestServiceDirective>("memory_service"); //create the client

    clientPtr = &client; //give the address of the client to the clientPtr

    ros::Subscriber sub = n.subscribe<vision::SceneTable>("scene_data", 1000, SUBSCRIBE_CALLBACK_FUNCTION); //subscribing

    ros::spin();
/*while(n.ok()) {

        ros::spinOnce();

    }*/
    return 0;
}
