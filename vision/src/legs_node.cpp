/**
 * Work in progress
 */

#include "ros/ros.h"
#include "ar_track_alvar_msgs/AlvarMarkers.h"
#include "tf/LinearMath/Transform.h"
#include "geometry_msgs/TransformStamped.h"
#include "sensor_msgs/Image.h"

#include <tf/transform_listener.h>
#include <math.h>
#include <stdlib.h>

#include "LegElaboration.h"
#include <vision/Configuration.h>
#include <vision/SceneTable.h>
#include <vision/Configuration_SIT.h>
#include <vision/SceneToSIT.h>

#include <opencv2/opencv.hpp>

#include <cv_bridge/cv_bridge.h>
#include <sensor_msgs/image_encodings.h>
//#include <sit_armor_injected_msgs/SceneElement.h>
//#include <sit_armor_injected_msgs/Recognition.h>
//#include <sit_armor_injected_msgs/SceneElementVector.h>

#include <fstream>
#include <string>
#include <boost/lexical_cast.hpp>
#include <boost/filesystem.hpp>
#include <vector>
#include <iostream> 
#include <string> 

# define ROWS 3
# define COLUMNS 12
//IN ORIGINE THR ERA 0.04
# define THR 0.04
# define NAMERELATION "isConnectedTo"
# define CONNECTED_THRESHOLD  0.07 // meters (positive number) IN ORIGINE ERA 0.1
using namespace ros;
using namespace tf;
using namespace ar_track_alvar_msgs;
using namespace std;
using namespace cv;

static int frameInstant=0;
static cv_bridge::CvImagePtr inputImage;
static string path_to_save_images="/home/fabio/java_workspace/src/vision/images/";
static long gamma_index=0;
//static int table=1;



void init_original_message(vision::SceneTable::Ptr a, vision::Configuration::Ptr b, struct configuration c){

    b->leg_id = c.leg_id;
    b->name_config = c.name_config;
    //b->degreeOrientation=c.degreeOrientation;
    b->pin=c.pin;
    //b->table="Table";
    //b->nameRelation=c.nameRelation;
    //b->pinTableRelationDegree=c.pinTableRelationDegree;
    //b->legPinRelationDegree=c.legPinRelationDegree;
    
    
   

    a->scene.push_back( *b);
   // a->frame=frameInstant;

}

void init_SIT_message(vision::SceneToSIT::Ptr a, vision::Configuration_SIT::Ptr b, struct configuration c, scene_struct &sceneStruct){

    gamma_index++;

    std::string gamma_leg="g_"+ boost::to_string(gamma_index);
    std::string gamma_pin="p_"+ boost::to_string(gamma_index);
    sceneStruct.gamma_leg= gamma_leg;
    sceneStruct.gamma_pin= gamma_pin;
    cout<<"NOME GAMMA LEG: " <<sceneStruct.gamma_leg<<"\n";
    cout<<"NOME GAMMA PIN: " <<sceneStruct.gamma_pin<<"\n";

    //For a leg
    b->gamma_i=gamma_leg; 
    b->type = c.name_config;
    b->degree=c.degreeOrientation;
    a->items.push_back( *b);

    //For a pin
    b->gamma_i=gamma_pin;
    b->type="Pin_"+ boost::to_string(c.pin);
    b->degree=1.0;
    a->items.push_back( *b);
       
   

    
    
    a->frame=frameInstant;

}

void add_table(vision::SceneToSIT::Ptr a, vision::Configuration_SIT::Ptr b, struct configuration c, scene_struct &sceneStruct){   

    //For a table
    //sceneStruct.gamma_table="t";
    //sceneStruct.type_table="Table";
    //cout<<"NOME GAMMA TABLE: " <<sceneStruct.gamma_table<<"\n";
    //cout<<"NOME TIPO TABLE: " <<sceneStruct.type_table<<"\n";

    b->gamma_i="t";
    b->type="Table";
    b->degree=1.0;

    a->items.push_back( *b);    

    

}



double distance(double xlegframe,double ylegframe,double xPin,double yPin){
    return sqrt((ylegframe - yPin) * (ylegframe - yPin) + (xlegframe - xPin) * (xlegframe - xPin));
}


double computeLegTableRelation (double xy [2]){
    double xLeg=xy[0];
    double yLeg=xy[1];
    double xTable=0;
    double yTable=0;
    double connection=distance(xLeg, yLeg, xTable,yTable);
    cout<<"CONNECTION TRA LEG E TABLE: " << connection <<"\n";			
   /* if (connection <= CONNECTED_THRESHOLD){
			
	//double degree = 1-(fabs(connection) / CONNECTED_THRESHOLD);
				
	return degree;
    }*/
	double degree = 1 - (fabs(connection) / 0.4);

return degree;
//return 0;

}


double computeLegLegRelation (double xyLeg1[2], double xyLeg2 [2]){
    double xLeg1=xyLeg1[0];
    double yLeg1=xyLeg1[1];
    double xLeg2=xyLeg2[0];
    double yLeg2=xyLeg2[1];
    double connection=distance(xLeg1, yLeg1, xLeg2,yLeg2);
    cout<<"CONNECTION TRA LEG E LEG: " << connection <<"\n";			
   if (connection <= CONNECTED_THRESHOLD){
			
	double degree = 1-(fabs(connection) / CONNECTED_THRESHOLD);
				
	return degree;
    }
	//double degree = 1 - (fabs(connection) / 0.4);

//return degree;
return 0;

}

double computePinPinRelation (double p[ROWS][COLUMNS], int pin1, int pin2){
	double xpin1;
	double ypin1;
	double xpin2;
	double ypin2;
     	for (int i = 0; i<COLUMNS; i++){ 
		if (i==pin1-1){ 
	 		xpin1=p[1][i];  
	 		ypin1=p[2][i];
			break;
		}
	}

	for (int i = 0; i<COLUMNS; i++){ 
		if (i==pin2-1){
			xpin2=p[1][i];  
	 		ypin2=p[2][i];
			break;
		}
	}
	
       	double connection=distance(xpin1,ypin1,xpin2,ypin2);
	cout<<"CONNECTION TRA PIN E PIN: " << connection <<"\n";         
       	if (connection <= CONNECTED_THRESHOLD){
					
        	double degree = 1-(fabs(connection) / CONNECTED_THRESHOLD);                        
			
	   	return degree;
	}
      	

return 0;

}

//LA NUOVA COMPUTELEGPINRELATION IN ORIGINE ERA: double computeLegPinRelation (double xy [2], double p[ROWS][COLUMNS], int pin, std::string name, std::string leg)

//NUOVA COMPUTELEGPINRELATION
double computeLegPinRelation (double xy [2], double p[ROWS][COLUMNS], int pin){
    double x=xy[0];
    double y=xy[1];

    ////è INUTILE PERCHÈ PASSO GIÀ, DALLA computeRelations, X E Y DELLE LEG MODIFICATI CON GLI STESSI CALCOLI IN EVAL_PIN PERCHÈ SALVATI NELLA 
    //// STRUCT
    /*if (name == "NOT_X" || name == "BED_X")
        x=x-0.115;
    else if (name == "NOT_MINUS_X" || name == "BED_MINUS_X")
        x=x+0.115;
    else if (name == "NOT_Y" || name == "BED_Y")
        y=y-0.115;
    else if (name == "NOT_MINUS_Y" || name == "BED_MINUS_Y")
        y=y+0.115;*/
    //////////////////////////////////////////////////////////////////////

//i is the name of the pin
    for (int i = 0; i<COLUMNS; i++){ 
	if (i==pin-1){     
       double connection=distance(x,y,p[1][i],p[2][i]);     
         //cout<<connection;
                //if( connectionNow < 0)
			//connectionNow = connectionNow * -1;
       		if (connection <= CONNECTED_THRESHOLD){
			//double degree = 1-(abs(connectionNow) / CONNECTED_THRESHOLD);
			
            		double degree = 1-(fabs(connection) / CONNECTED_THRESHOLD);
                        
			
			
                       //ROS_INFO("\n\n***** %s %s to pin %d with degree %f with connection: %f *****", conf_leg.leg_id.c_str(), NAMERELATION, i+1, degree, connectionNow);
	    		return degree;
		}
      	}
    }
//ROS_ERROR("   !!!!!!%s not connected to a PIN!!!!!!\n", leg.c_str());
return 0;
}


//NUOVA COMPUTEPINTABLERELATION
double computePinTableRelation(double p[ROWS][COLUMNS], int pin){
        double xTable=0;
	double yTable=0;
        
	for (int i = 0; i<COLUMNS; i++){ 
		if (i==pin-1){
			double connection=distance(xTable,yTable,p[1][i],p[2][i]);
			//cout<<"\n"<<connection<<"\n";
			double degree = 1 - (fabs(connection) / 0.4);	
			return degree;
		}
	}
}



void computeRelations(vision::SceneToSIT::Ptr a, double pins[ROWS][COLUMNS], vector<scene_struct> structVector, vision::Relations::Ptr b, struct relation r){

	double xy_leg1[2];
	double xy_leg2[2];
	int pin1;
	int pin2;
	std:string toErase="Pin_";
	int actual_loop_size=0;

	for(vector<scene_struct>::iterator it = structVector.begin(); it != structVector.end(); ++it){

			actual_loop_size++;
		
			/*cout<<"\n";
			cout<<"DENTRO LA COMPUTERELATIONS"<<"\n";
			cout<<"\n";
			//cout<<"SIZE ARRAY NOW: " << sceneStructVector.size()<<"\n";
			cout<<"NOME GAMMA LEG NELL'ARRAY: "<<it->gamma_leg<<"\n";
		        cout<<"TIPO LEG NELL'ARRAY: "<<it->type_leg<<"\n";
			cout<<"X LEG NELL'ARRAY: "<<it->x_leg<<"\n";
		        cout<<"Y LEG NELL'ARRAY: "<<it->y_leg<<"\n";
			cout<<"GAMMA PIN NELL'ARRAY: "<<it->gamma_pin<<"\n";
			cout<<"TIPO PIN NELL'ARRAY: "<<it->type_pin<<"\n";
			cout<<"GAMMA TABLE NELL'ARRAY: "<<it->gamma_table<<"\n";
			cout<<"TIPO TABLE NELL'ARRAY: "<<it->type_table<<"\n";
			cout<<"\n";*/

		xy_leg1[0]=it->x_leg;
		xy_leg1[1]=it->y_leg;
		std::string pin_type=it->type_pin;
		size_t pos = pin_type.find(toErase);
	 
		if (pos != std::string::npos)
		{
			// If found then erase it from string
			pin_type.erase(pos, toErase.length());
		}
	
		pin1=std::stoi(pin_type);

		///////////////Relations between a leg and a pin in a single scene_struct//////////
		b->gamma_subject=it->gamma_leg;
		b->gamma_object=it->gamma_pin;
		b->nameRelation=NAMERELATION;
		b->degreeRelation=computeLegPinRelation(xy_leg1,pins, pin1);
		a->relations.push_back(*b);

		////////////////////////////////////////////////////////////////////////////////////

		//////////////Relations between a pin and the table in a single scene_struct///////
		b->gamma_subject=it->gamma_pin;
		b->gamma_object="t";
		b->nameRelation=NAMERELATION;
		b->degreeRelation=computePinTableRelation(pins, pin1);
		a->relations.push_back(*b);
	
		//////////////////////////////////////////////////////////////////////////////////

		//////////////Relations between a leg and the table in a single scene_struct///////

		b->gamma_subject=it->gamma_leg;
		b->gamma_object="t";
		b->nameRelation=NAMERELATION;
		b->degreeRelation=computeLegTableRelation(xy_leg1);	
		a->relations.push_back(*b);

		//////////////////////////////////////////////////////////////////////////////////

		/////Loop to compute the relations intra-struct elements//////////////////////////
		for(int i = 1; i < structVector.size(); i++){
			if(i!=actual_loop_size){
				xy_leg2[0]=structVector[i-1].x_leg;
				xy_leg2[1]=structVector[i-1].y_leg;
				std::string pin_type2=structVector[i-1].type_pin;
				size_t pos2 = pin_type2.find(toErase);
	 
				if (pos2 != std::string::npos)
				{
					// If found then erase it from string
					pin_type2.erase(pos2, toErase.length());
				}
	
				pin2=std::stoi(pin_type2);
	
				///////////////Relations between a leg and a pin between different scene_struct//////////

				b->gamma_subject=it->gamma_leg;
				b->gamma_object=structVector[i-1].gamma_pin;
				b->nameRelation=NAMERELATION;
				b->degreeRelation=computeLegPinRelation(xy_leg1,pins, pin2);
				a->relations.push_back(*b);

				////////////////////////////////////////////////////////////////////////////////////////

				///////////////Relations between legs between different scene_struct////////////////////

				b->gamma_subject=it->gamma_leg;
				b->gamma_object=structVector[i-1].gamma_leg;
				b->nameRelation=NAMERELATION;
				b->degreeRelation= computeLegLegRelation(xy_leg1, xy_leg2);
				a->relations.push_back(*b);

				///////////////////////////////////////////////////////////////////////////////////////

				///////////////Relations between pins between different scene_struct////////////////////

				b->gamma_subject=it->gamma_pin;
				b->gamma_object=structVector[i-1].gamma_pin;
				b->nameRelation=NAMERELATION;
				b->degreeRelation=computePinPinRelation(pins, pin1, pin2);			
				a->relations.push_back(*b);

				///////////////////////////////////////////////////////////////////////////////////////			


			}
		} 	
	
	
	}


}



/*void init_msg_for_Armor(sit_armor_injected_msgs::SceneElement::Ptr leg, sit_armor_injected_msgs::SceneElement::Ptr pin, sit_armor_injected_msgs::SceneElementVector::Ptr msg, struct configuration c,
                        double xy[2],double p[ROWS][COLUMNS] ){

    const double x=xy[0];
    const double y=xy[1];
    leg->type=c.name_config;
    leg->features.push_back(x);
    leg->features.push_back(y);
    leg->features.push_back(0.0);

    msg->element.push_back( *leg);

    const double px = p[1][c.pin-1];
    const double py = p[2][c.pin-1];
    pin->type = "PIN_" + boost::to_string(c.pin);
    pin->features.push_back(px);
    pin->features.push_back(py);
    pin->features.push_back(0.0);

    msg->element.push_back( *pin);

}*/




/////////////CALLBACK FOR THE IMAGES/////////////////////////////////
/*void callback_save_image (const sensor_msgs::ImageConstPtr& msg){
//cv_bridge::CvImagePtr cv_ptr;
//cout<<"ENTRA NELLA CALLBACK";
    try
    {
      inputImage = cv_bridge::toCvCopy(msg,sensor_msgs::image_encodings::TYPE_8UC1);
    }
    catch (cv_bridge::Exception& e)
    {
      ROS_ERROR("cv_bridge exception: %s", e.what());
      return;
    }
// inputImage=cv_ptr;
}*/




void initialize_pins_position (double p[ROWS][COLUMNS]){

    for (int i = 0; i<12 ; i++){
        p[0][i]=i+1;
    }

    p[1][0]=-0.30;
    p[2][0]=-0.16;

    p[1][1]=-0.05;
    p[2][1]=-0.17;

    p[1][2]=0.03;
    p[2][2]=-0.15;

    p[1][3]=0.27;
    p[2][3]=-0.17;

    p[1][4]=0.24;
    p[2][4]=-0.02;

    p[1][5]=0.24;
    p[2][5]=0.05;

    p[1][6]=0.27;
    p[2][6]=0.19;

    p[1][7]=0.04;
    p[2][7]=0.17;

    p[1][8]=-0.04;
    p[2][8]=0.17;

    p[1][9]=-0.28;
    p[2][9]=0.20;

    p[1][10]=-0.24;
    p[2][10]=0.05;

    p[1][11]=-0.28;
    p[2][11]=-0.05;
}







//VECCHIA COMPUTEPINTABLERELATION
/*int computeLegPinRelation (configuration &conf_leg,double p[ROWS][COLUMNS],double xlegframe,double ylegframe){
//i is the name of the pin
    for (int i = 0; i<COLUMNS; i++){       
       double connectionNow=distance(xlegframe,ylegframe,p[1][i],p[2][i]);
       double connectionBefore;
       if(connectionNow<=connectionBefore){
       	connectionBefore=connectionNow;
                //if( connectionNow < 0)
			//connectionNow = connectionNow * -1;
       		if (connectionNow <= CONNECTED_THRESHOLD){
			//double degree = 1-(abs(connectionNow) / CONNECTED_THRESHOLD);
			
            		double degree = 1-(fabs(connectionNow) / CONNECTED_THRESHOLD);
                        
			conf_leg.legPinRelationDegree=degree;
			
                       //ROS_INFO("\n\n***** %s %s to pin %d with degree %f with connection: %f *****", conf_leg.leg_id.c_str(), NAMERELATION, i+1, degree, connectionNow);
	    		return i+1;
       		}
      	}
    }
ROS_ERROR("   !!!!!!%s not connected to a PIN!!!!!!\n", conf_leg.leg_id.c_str());
return 0;
}*/










//VECCHIA COMPUTEPINTABLERELATION
/*double computePinTableRelation(configuration &conf_leg, double p[ROWS][COLUMNS], int pin){
	double xTable=0.0;
	double yTable=0.0;
        
	for (int i = 0; i<COLUMNS; i++){ 
		if (i==pin-1){
			double connection=distance(xTable,yTable,p[1][i],p[2][i]);
//CALCOLANDO CON LA CALCOLATRICE NON VIENE DEGREE 1 MA -2... PERCIò NON CAPITO COME FA A VENIRE 1 CHE è CIò CHE PASSA
				//if( connection < 0)
					//connection = connection * -1;
				double degree = 1 - (fabs(connection) / 0.4);
				//ROS_INFO("\n\n***** Table %s pin %d with degree %f with connection %f*****", NAMERELATION, pin, degree, connection);
				return degree;
			//}
		}
	}

	//return 0;
}*/




//QUESTA FUNZIONE EVAL_PIN ERA GIà PRESENTE
int eval_pin (double xy [2], double p[ROWS][COLUMNS], std::string name, std::string leg, scene_struct &sceneStruct_Leg){

    double x=xy[0];
    double y=xy[1];
    cout<<"X ENTRA NELLA FUNZIONE: "<< x<<"\n";
    cout<<"Y ENTRA NELLA FUNZIONE: "<<y<<"\n";

    if (name == "NOT_X" || name == "BED_X")
        x=x-0.115;
    else if (name == "NOT_MINUS_X" || name == "BED_MINUS_X")
        x=x+0.115;
    else if (name == "NOT_Y" || name == "BED_Y")
        y=y-0.115;
    else if (name == "NOT_MINUS_Y" || name == "BED_MINUS_Y")
        y=y+0.115;

    cout<<"X ESCE DAGLI IF: "<<x<<"\n";
    cout<<"Y ESCE DAGLI IF: "<<y<<"\n";

    sceneStruct_Leg.x_leg=x;
    sceneStruct_Leg.y_leg=y;


    cout<<"VALORE NELLA STRUCT X: " <<sceneStruct_Leg.x_leg<<"\n";
    cout<<"VALORE NELLA STRUCT Y: " <<sceneStruct_Leg.y_leg<<"\n";


    
    int pin=0;
    for (int i = 0; i<COLUMNS; i++){
        if (x<p[1][i]+THR && x>p[1][i]-THR){
            if (y<p[2][i]+THR && y>p[2][i]-THR){
                pin = i+1;
                cout<<"PIN TROVATO: " <<pin <<"\n";
                sceneStruct_Leg.type_pin="Pin_"+ boost::to_string(pin);
                cout<<"PIN ENTRA NELLA FUNZIONE: "<<sceneStruct_Leg.type_pin<<"\n";
                
                //ROS_INFO("\n\n***** %s connected to pin %d *****", leg.c_str(), pin);
                return pin;
            }
	}
    }

    ROS_ERROR("   !!!!!!%s not connected to a PIN!!!!!!\n", leg.c_str());
    return 0;
}

//QUESTA è LA MIA FUNZIONE EVAL_PIN
/*int eval_pin (double xy [2], double p[ROWS][COLUMNS], configuration &conf_leg){
    //Degree to evaluate how much the most probable connected pin is connected to the specific leg
    
    double xlegframe=xy[0];
    double ylegframe=xy[1];
    if (conf_leg.name_config == "NOT_X" || conf_leg.name_config == "BED_X")
        xlegframe=xlegframe-0.115;
    else if (conf_leg.name_config == "NOT_MINUS_X" || conf_leg.name_config == "BED_MINUS_X")
        xlegframe=xlegframe+0.115;
    else if (conf_leg.name_config == "NOT_Y" || conf_leg.name_config == "BED_Y")
        ylegframe=ylegframe-0.115;
    else if (conf_leg.name_config == "NOT_MINUS_Y" || conf_leg.name_config == "BED_MINUS_Y")
        ylegframe=ylegframe+0.115; 
    int pin=computeLegPinRelation(conf_leg,p,xlegframe,ylegframe); 
    return pin;
    
       
        /*if (x<p[1][i]+THR && x>p[1][i]-THR)
            if (y<p[2][i]+THR && y>p[2][i]-THR){
                pin = i+1;
                //ROS_INFO("\n\n***** %s connected to pin %d *****", leg.c_str(), pin);
                return pin;
            }*/
            
    //ROS_ERROR("   !!!!!!%s not connected to a PIN!!!!!!\n", leg.c_str());
   // return 0;
//}



void eval_config (double angles[3],tf::StampedTransform t, double xy[2], double pins[ROWS][COLUMNS], configuration &conf_leg, std::string leg_name, scene_struct &sceneStruct_Leg){
	
    
    tf::Matrix3x3 m0(t.getRotation());
    m0.getEulerYPR(angles[2], angles[1], angles[0]);
//roll
    angles[0] = angles[0] * (180 / M_PI);
//pitch
    angles[1] = angles[1] * (180 / M_PI);
//yaw
    angles[2] = angles[2] * (180 / M_PI);
    change_angle_interval(angles);
//After "check_configuration" function I know the type of the leg and the orientation respect to the WORLD frame
    check_configuration(angles, conf_leg, leg_name);
    sceneStruct_Leg.type_leg=conf_leg.name_config;
    
    ROS_INFO("Conf leg %s:  %s", leg_name.c_str(), conf_leg.name_config.c_str());
    cout<<"TIPO DI LEG NELLA STRUCT: " << sceneStruct_Leg.type_leg <<"\n";
    xy[0]=t.getOrigin().x();
    xy[1]=t.getOrigin().y();
    conf_leg.pin=eval_pin(xy, pins, conf_leg.name_config, conf_leg.leg_id, sceneStruct_Leg);



    /*conf_leg.legPinRelationDegree=computeLegPinRelation(xy,pins,conf_leg.pin,conf_leg.name_config,conf_leg.leg_id); 
    conf_leg.pinTableRelationDegree=computePinTableRelation(pins, conf_leg.pin);
    conf_leg.nameRelation=NAMERELATION;*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//PER LA MIA FUNZIONE (QUELLA VECCHIA) è QUESTA PARTE SOTTO DA DECOMMENTARE
    /*int pin=eval_pin(xy, pins, conf_leg);
    conf_leg.pin=pin;
    conf_leg.nameRelation=NAMERELATION;*/
//PRINT FOR DEBUGGING
    //ROS_INFO("\n%s\n%s\nCONNECTED TO PIN %d\n", conf_leg.leg_id.c_str(), conf_leg.name_config.c_str(), conf_leg.pin);
    //ROS_INFO("\n%s\n%s\n%s TO PIN %d WITH DEGREE %f\n", conf_leg.leg_id.c_str(), conf_leg.name_config.c_str(), conf_leg.nameRelation, pin, conf_leg.legPinRelationDegree);
    //conf_leg.pinTableRelationDegree=computePinTableRelation(conf_leg,pins,pin);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    

    
}



/**
 * This is the main function where we declare the nodehandles and relevant subscribers and publishers
 * @param argc : The standard inputs
 * @param argv : The standard inputs
 * @return : A return value
 */


int main(int argc, char **argv)
{
    ros::init(argc, argv, "legs_node");
    ros::NodeHandle n;

    
    string folder_name;
    string file_name;

    std::fstream fxy;
    fxy.open("xy.txt", std::fstream::out);
    std::fstream fypr;
    fypr.open("ypr.txt", std::fstream::out);
    std::fstream f;
    f.open("config.txt", std::fstream::out);

    //get data from tf: transforms between world and leg markers
    tf::TransformListener listener0;
    tf::TransformListener listener4;
    tf::TransformListener listener8;
    tf::TransformListener listener12;

    //PROVATO CON 100 MA NESSUN RISULTATO
    //PRIMA IN ORIGINE ERA 10
     //Publisher for data from tf
     ros::Publisher Scene_pub = n.advertise<vision::SceneTable>("scene_data", 10);

//////////////////////////////////////////////////////////////////////////////////
     ros::Publisher SceneSIT_pub = n.advertise<vision::SceneToSIT>("sceneSIT_data", 10);
///////////////////////////////////////////////////////////////////////////////////

     //Subscriber for the images
    //ros::Subscriber sub = n.subscribe("kinect2/qhd/image_mono", 1000, callback_save_image);


    




    //ros::Publisher Scene_pub_4Armor = n.advertise<sit_armor_injected_msgs::SceneElementVector>("scene_data_4Armor", 100);

//PROVATO CON 20 MA NESSUN RISULTATO 
//ORIGINALE è 10 di rate
    ros::Rate rate(10.0);

    double xy_100[2];
    double xy_104[2];
    double xy_108[2];
    double xy_112[2];
    double angles_100[3];
    double angles_104[3];
    double angles_108[3];
    double angles_112[3];


    double pins[ROWS][COLUMNS];

    initialize_pins_position(pins);

    struct configuration conf_leg0;
    struct configuration conf_leg4;
    struct configuration conf_leg8;
    struct configuration conf_leg12;
    ///////NEW STRUCTS FOR RELATIONS
    struct relation relationInScene;
    struct scene_struct sceneStruct_Leg0;
    struct scene_struct sceneStruct_Leg4;
    struct scene_struct sceneStruct_Leg8;
    struct scene_struct sceneStruct_Leg12;
    vector<scene_struct> sceneStructVector;
    //struct scene_struct sceneStructArray [4];
    int leg_counter=0;   
    
    int k=1;
    //Variables used to create the folders to save the images by basing on the parameter in the Parameter Server which have the name of the bag file and the folder name of it
    char sep='/';
    size_t index_position;
    int stat;
    string dirname;
    std:string toErase=".bag";
    size_t pos;

    while(n.ok()) {

    bool isThereATable=false;
    //bool *tablePtr=&isThereATable;
    
//DURATA ORIGINALE è 1
//CON 8 IL RISULTATO SEMBRA ACCETTABILE MA POI TORNA A QUELLO CHE NON DEVE VENIRE DOPO CHE IL BAG è TERMINATO
        ros::Duration(1.0).sleep();
        tf::StampedTransform transform_w_100;
        tf::StampedTransform transform_w_104;
        tf::StampedTransform transform_w_108;
        tf::StampedTransform transform_w_112;
        vision::SceneTable::Ptr ourScene (new vision::SceneTable);
/////////////////////////////////////////////////////////////////
        vision::SceneToSIT::Ptr ourSceneToSIT (new vision::SceneToSIT);
/////////////////////////////////////////////////////////////////////	
       


 //sit_armor_injected_msgs::SceneElementVector::Ptr sceneForArmor(new sit_armor_injected_msgs::SceneElementVector);


/////////////////////////Piece of code to take the image from the bag file convert it to cv_bridge andsave it/////////////////////////	
	

	//string name_image= path_to_save_images+to_string(frameInstant)+".jpg";
	frameInstant++;

	/*n.getParam("/folder_name", folder_name);
    	n.getParam("/file_name",file_name);
       
   	
    	cout<<"File Name: " << file_name <<"\n";
    	cout<<"Folder Name:"<<folder_name<<"\n";
        cout<<"\n";

	index_position = folder_name.rfind(sep, folder_name.length());

    	if (index_position != string::npos) {
      		folder_name=folder_name.substr(index_position+1, folder_name.length() - index_position);
    	}
    	index_position = file_name.rfind(sep, file_name.length());
    	if (index_position != string::npos) {
      		file_name=file_name.substr(index_position+1, file_name.length() - index_position);
    	}
    		cout<<"Folder name: "<<folder_name<<"\n";
    		cout<<"File name: "<<file_name<<"\n";

	

	dirname=path_to_save_images+folder_name;
	// Search for the substring in string
	
	pos = dirname.find(toErase);
 
	if (pos != std::string::npos)
	{
		// If found then erase it from string
		dirname.erase(pos, toErase.length());
	}
	cout<<"DIRNAME: "<<dirname<<"\n";
   	/*stat = boost::filesystem::create_directory(dirname);
	if (!stat)
      		ROS_INFO("Directory %s created\n",dirname);
   	else
   	{
      		ROS_ERROR("Unable to create directory %s\n", dirname);
      
   	}*/

	//cout<<inputImage;
	/*if (inputImage){
	string frame = boost::lexical_cast<string>(frameInstant);
	
	string name_image= dirname + "_" + frame + ".png";
	
	imwrite(name_image, inputImage->image);
	inputImage.reset();
	}
	cout<<"\n";
	ROS_INFO("\nTHE FRAME NOW IS: %d", frameInstant);
	//cout<<"\n";*/

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







        for (int i = 0; i<3; i++){
            angles_100[i];
            angles_104[i];
            angles_108[i];
            angles_112[i];
        }
        for (int j = 0; j<2; j++){
            xy_100[j]=0;
            xy_104[j]=0;
            xy_108[j]=0;
            xy_112[j]=0;
        }

        try {
            listener0.waitForTransform("/WORLD", "/ar_marker_100", ros::Time(0), ros::Duration(0.00005));
            listener0.lookupTransform("/WORLD", "/ar_marker_100", ros::Time(0), transform_w_100);
                        
            eval_config(angles_100,transform_w_100, xy_100, pins, conf_leg0, "Leg_0", sceneStruct_Leg0);

	    
            if (conf_leg0.pin > 0 && conf_leg0.name_config.size()>0) {
                f << conf_leg0.leg_id << std::endl << conf_leg0.name_config << std::endl << "Pin:" << conf_leg0.pin << std::endl << std::endl;
                fypr << angles_100[2] <<" " << angles_100[1] <<" " << angles_100[0] << std::endl;
                fxy << "leg 0 : " << xy_100[0] <<" " << xy_100[1] << std:: endl;

		

                vision::Configuration::Ptr msg0(new vision::Configuration);
///////////////////////////////////////////////////////////////////////////////////////
                vision::Configuration_SIT::Ptr msg0SIT(new vision::Configuration_SIT);
		
////////////////////////////////////////////////////////////////////////////////////////

                init_original_message(ourScene, msg0, conf_leg0);

///////////////////////////////////////////////////////////////////////////////////////////////
                
		init_SIT_message(ourSceneToSIT, msg0SIT, conf_leg0, sceneStruct_Leg0);
		if (isThereATable==false){
			add_table(ourSceneToSIT, msg0SIT, conf_leg0, sceneStruct_Leg0);
			//*tablePtr=true;
			isThereATable=true;
		}
		sceneStructVector.push_back(sceneStruct_Leg0);
		//sceneStructArray[leg_counter]=sceneStruct_Leg0;
//////////////////////////////////////////////////////////////////////////
                /*sit_armor_injected_msgs::SceneElement::Ptr ar0(new sit_armor_injected_msgs::SceneElement);
                sit_armor_injected_msgs::SceneElement::Ptr pin0(new sit_armor_injected_msgs::SceneElement);
                init_msg_for_Armor(ar0, pin0, sceneForArmor, conf_leg0, xy_100, pins);*/
            }
        }

        catch (tf::TransformException &ex100) {
            ROS_ERROR("%s", ex100.what());
        }




        try {
            listener4.waitForTransform("/WORLD", "/ar_marker_104", ros::Time(0), ros::Duration(0.00005));
            listener4.lookupTransform("/WORLD", "/ar_marker_104", ros::Time(0), transform_w_104);
            eval_config(angles_104,transform_w_104, xy_104, pins, conf_leg4, "Leg_4", sceneStruct_Leg4);

            if (conf_leg4.pin > 0 && conf_leg4.name_config.size()>0){
                f << conf_leg4.leg_id << std::endl << conf_leg4.name_config << std::endl << "Pin:" << conf_leg4.pin << std::endl << std::endl;
                fypr << angles_104[2] <<" " << angles_104[1] <<" " << angles_104[0] << std::endl;
                fxy << "leg 4: " << xy_104[0] <<" " << xy_104[1] << std:: endl;


                vision::Configuration::Ptr msg4(new vision::Configuration);
///////////////////////////////////////////////////////////////////////////////////////
                vision::Configuration_SIT::Ptr msg4SIT(new vision::Configuration_SIT);
////////////////////////////////////////////////////////////////////////////////////////
                
		init_original_message(ourScene, msg4, conf_leg4);



///////////////////////////////////////////////////////////////////////////////////////////////
                
		init_SIT_message(ourSceneToSIT, msg4SIT, conf_leg4, sceneStruct_Leg4);
		if (isThereATable==false){
                	add_table(ourSceneToSIT, msg4SIT, conf_leg4, sceneStruct_Leg4);
			isThereATable=true;
		}
                //leg_counter++;
		sceneStructVector.push_back(sceneStruct_Leg4);
                //sceneStructArray[leg_counter]=sceneStruct_Leg4;
//////////////////////////////////////////////////////////////////////////
		
                /*sit_armor_injected_msgs::SceneElement::Ptr ar4(new sit_armor_injected_msgs::SceneElement);
                sit_armor_injected_msgs::SceneElement::Ptr pin4(new sit_armor_injected_msgs::SceneElement);
                init_msg_for_Armor(ar4, pin4, sceneForArmor, conf_leg4, xy_104, pins);*/
            }
        }

        catch (tf::TransformException &ex104) {
            ROS_ERROR("%s", ex104.what());
        }

        try {
            listener8.waitForTransform("/WORLD", "/ar_marker_108", ros::Time(0), ros::Duration(0.00005));
            listener8.lookupTransform("/WORLD", "/ar_marker_108", ros::Time(0), transform_w_108);
            eval_config(angles_108,transform_w_108, xy_108, pins, conf_leg8, "Leg_8", sceneStruct_Leg8);

            if (conf_leg8.pin > 0 && conf_leg8.name_config.size()>0){
                f << conf_leg8.leg_id << std::endl << conf_leg8.name_config << std::endl << "Pin:" << conf_leg8.pin << std::endl << std::endl;
                fypr << angles_108[2] <<" " << angles_108[1] <<" " << angles_108[0] << std::endl;
                fxy << "leg 8: " << xy_108[0] <<" " << xy_108[1] << std:: endl;
		
		leg_counter++;
                vision::Configuration::Ptr msg8(new vision::Configuration);

///////////////////////////////////////////////////////////////////////////////////////
                vision::Configuration_SIT::Ptr msg8SIT(new vision::Configuration_SIT);
////////////////////////////////////////////////////////////////////////////////////////
                
		init_original_message(ourScene, msg8, conf_leg8);

///////////////////////////////////////////////////////////////////////////////////////////////
                
		init_SIT_message(ourSceneToSIT, msg8SIT, conf_leg8, sceneStruct_Leg8);
		if (isThereATable==false){
			add_table(ourSceneToSIT, msg8SIT, conf_leg8, sceneStruct_Leg8);
			isThereATable=true;
		}
               // leg_counter++;
		sceneStructVector.push_back(sceneStruct_Leg8);
                //sceneStructArray[leg_counter]=sceneStruct_Leg8;
//////////////////////////////////////////////////////////////////////////

                /*sit_armor_injected_msgs::SceneElement::Ptr ar8(new sit_armor_injected_msgs::SceneElement);
                sit_armor_injected_msgs::SceneElement::Ptr pin8(new sit_armor_injected_msgs::SceneElement);
                init_msg_for_Armor(ar8, pin8, sceneForArmor, conf_leg8, xy_108, pins);*/
            }

        }

        catch (tf::TransformException &ex108) {
            ROS_ERROR("%s", ex108.what());
        }

        try {
            listener12.waitForTransform("/WORLD", "/ar_marker_112", ros::Time(0), ros::Duration(0.00005));
            listener12.lookupTransform("/WORLD", "/ar_marker_112", ros::Time(0), transform_w_112);
            eval_config(angles_112,transform_w_112, xy_112, pins, conf_leg12, "Leg_12", sceneStruct_Leg12);

            if (conf_leg12.pin > 0 && conf_leg12.name_config.size()>0) {
                f << conf_leg12.leg_id << std::endl << conf_leg12.name_config << std::endl <<"Pin:" << conf_leg12.pin << std::endl << std::endl;
                fypr << angles_112[2] <<" " << angles_112[1] <<" " << angles_112[0] << std::endl;
                fxy << "leg 12: " << xy_112[0] <<" " << xy_112[1] << std:: endl;


                vision::Configuration::Ptr msg12(new vision::Configuration);
///////////////////////////////////////////////////////////////////////////////////////
                vision::Configuration_SIT::Ptr msg12SIT(new vision::Configuration_SIT);
////////////////////////////////////////////////////////////////////////////////////////

                init_original_message(ourScene, msg12, conf_leg12);

///////////////////////////////////////////////////////////////////////////////////////////////
                
		init_SIT_message(ourSceneToSIT, msg12SIT, conf_leg12, sceneStruct_Leg12);
		if (isThereATable==false){
                	add_table(ourSceneToSIT, msg12SIT, conf_leg12, sceneStruct_Leg12);
			isThereATable=true;
		}
                //leg_counter++;
		sceneStructVector.push_back(sceneStruct_Leg12);
                //sceneStructArray[leg_counter]=sceneStruct_Leg12;

//////////////////////////////////////////////////////////////////////////

                /*sit_armor_injected_msgs::SceneElement::Ptr ar12(new sit_armor_injected_msgs::SceneElement);
                sit_armor_injected_msgs::SceneElement::Ptr pin12(new sit_armor_injected_msgs::SceneElement);
                init_msg_for_Armor(ar12, pin12, sceneForArmor, conf_leg12, xy_112, pins);*/
            }
        }

        catch (tf::TransformException &ex112) {
            ROS_ERROR("%s", ex112.what());
        }
	///////FUNZIONE CALCOLO DEGREE

        //Scene_pub_4Armor.publish(sceneForArmor);
        //PRINT FOR DEBUGGING
	//cout<<*ourScene;
	/*for(vector<scene_struct>::iterator it = sceneStructVector.begin(); it != sceneStructVector.end(); ++it){

		//cout<<"NOME TIPO LEG NELL'ARRAY: "<<type_leg<<"\n";
			cout<<"\n";
			cout<<"FUORI LA COMPUTERELATIONS"<<"\n";
			cout<<"\n";
			cout<<"SIZE ARRAY NOW: " << sceneStructVector.size()<<"\n";
			cout<<"NOME GAMMA LEG NELL'ARRAY: "<<it->gamma_leg<<"\n";
		        cout<<"TIPO LEG NELL'ARRAY: "<<it->type_leg<<"\n";
			cout<<"X LEG NELL'ARRAY: "<<it->x_leg<<"\n";
		        cout<<"Y LEG NELL'ARRAY: "<<it->y_leg<<"\n";
			cout<<"GAMMA PIN NELL'ARRAY: "<<it->gamma_pin<<"\n";
			cout<<"TIPO PIN NELL'ARRAY: "<<it->type_pin<<"\n";
			cout<<"GAMMA TABLE NELL'ARRAY: "<<it->gamma_table<<"\n";
			cout<<"TIPO TABLE NELL'ARRAY: "<<it->type_table<<"\n";
			cout<<"\n";
	  
		
	
	}*/

	vision::Relations::Ptr relation(new vision::Relations);
	computeRelations(ourSceneToSIT, pins, sceneStructVector, relation, relationInScene);
	sceneStructVector.clear();
       /*struct scene_struct LEG=sceneStruct_Leg0;
	
	
	//computeRelations(ourSceneToSIT);
	//cout<<sceneStruct_Leg0;
        cout<<LEG.gamma_leg<<"\n";
  	cout<<LEG.type_leg<<"\n";
  	cout<<LEG.x_leg<<"\n";
  	cout<<LEG.y_leg<<"\n";
  	cout<<LEG.gamma_pin<<"\n";
  	cout<<LEG.type_pin<<"\n";
  	cout<<LEG.gamma_table<<"\n";
  	cout<<LEG.type_table<<"\n";
	/*cout<<sceneStruct_Leg4;
	cout<<sceneStruct_Leg8;
	cout<<sceneStruct_Leg12;*/

        Scene_pub.publish(ourScene);
	SceneSIT_pub.publish(ourSceneToSIT);
	
        f << "-------------------"<< std::endl << k << std::endl;
        k++;
	
        ros::spinOnce();
        
	
	rate.sleep();
    }
    return 0;
}

