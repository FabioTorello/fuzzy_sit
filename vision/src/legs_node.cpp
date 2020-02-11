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
#include <map> 

# define ROWS 3
# define COLUMNS 12
//IN ORIGINE THR ERA 0.04
# define THR 0.04
# define NAMERELATION "isConnectedTo"
# define CONNECTED_THRESHOLD  0.07 // meters (positive number) IN ORIGINE ERA 0.1
#define NUMBER_OBJECTS 10

using namespace ros;
using namespace tf;
using namespace ar_track_alvar_msgs;
using namespace std;
using namespace cv;

static int frameInstant=0;
static cv_bridge::CvImagePtr inputImage;
static string path_to_save_images="/home/fabio/java_workspace/src/vision/images/";
static long gamma_index=0;
struct item itemStruct;
struct relation relationStruct;
vector<item> itemStructVector;
vector<relation> relationStructVector;




void init_original_message(vision::SceneTable::Ptr a, vision::Configuration::Ptr b, struct configuration c){

    b->leg_id = c.leg_id;
    b->name_config = c.name_config;
    b->pin=c.pin;
   
    
    
   

    a->scene.push_back( *b);
   

}

void init_SIT_message(vision::SceneToSIT::Ptr a, vision::Configuration_SIT::Ptr b, vision::Relations::Ptr r, vector<item> itemStructVector, vector<relation> relationStructVector){

	for(vector<item>::iterator it = itemStructVector.begin(); it != itemStructVector.end(); ++it){

		
			/*cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"VECTOR OF ITEMS IN SIT MESSAGE"<<"\n";
			cout<<"SIZE ARRAY NOW: " << itemStructVector.size()<<"\n";
			cout<<"GAMMA NELL'ARRAY DI ITEMS: "<<it->gamma_i<<"\n";		        
			cout<<"TYPE NELL'ARRAY DI ITEMS: "<<it->type<<"\n";
		        cout<<"DEGREE NELL'ARRAY DI ITEMS: "<<it->degree<<"\n";
			
			cout<<"\n";*/

			b->gamma_i=it->gamma_i; 
    			b->type = it->type;
    			b->degree=it->degree;
    			a->items.push_back( *b);
	  
		
	
	}

	for(vector<relation>::iterator it_rel = relationStructVector.begin(); it_rel != relationStructVector.end(); ++it_rel){

		
			/*cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"VECTOR OF RELATIONS IN SIT MESSAGE"<<"\n";
			cout<<"SIZE ARRAY NOW: " << relationStructVector.size()<<"\n";			
			cout<<"GAMMA SUBJECT: "<<it_rel->gamma_subject<<"\n";
			cout<<"GAMMA OBJECT: "<<it_rel->gamma_object<<"\n";	
			cout<<"NAME RELATION: "<<it_rel->nameRelation<<"\n";	        
		        cout<<"DEGREE RELATION: "<<it_rel->degreeRelation<<"\n";
			
			cout<<"\n";*/
			
				r->gamma_subject=it_rel->gamma_subject;
				r->gamma_object=it_rel->gamma_object;
				r->nameRelation=it_rel->nameRelation;
				r->degreeRelation=it_rel->degreeRelation;
				a->relations.push_back(*r);
	  
		
	
	}

	a->frame=frameInstant;

}






double distance(double xlegframe,double ylegframe,double xPin,double yPin){
    return sqrt((ylegframe - yPin) * (ylegframe - yPin) + (xlegframe - xPin) * (xlegframe - xPin));
}


double computeRelation (double x1, double y1, double x2, double y2, float threshold){
   
    double connection=distance(x1, y1, x2,y2);
    //cout<<"CONNECTION: " << connection <<"\n";			
    if (connection <= threshold){
			
	double degree = 1-(fabs(connection) / threshold);
				
	return degree;
    }
	

return 0;

}

void computeAllRelations(vector<object> &objectVector){
	double degree;
	double relationDegree;
	for (int i=0; i<objectVector.size();i++){
		for (int j=0; j<objectVector.size();j++){

			if(j!=i && j>i){
				//cout<< "i: " << i << "--"<< "j: " << j << "\n";
				//cout<<"--------------------------"<<"\n";
				if((objectVector[i].name.find("g")!=std::string::npos && objectVector[j].name.find("p")!=std::string::npos) || (objectVector[i].name.find("p")!=std::string::npos && objectVector[j].name.find("g")!=std::string::npos) ){
					//cout<<"\n";
					//cout<<"Entra in p-g g-p "<<"\n";
					//cout<<"Subject: "<< objectVector[i].name<<"\n";
					//cout<<"Object: "<< objectVector[j].name<<"\n";
					
					//Subject is a leg
					relationStruct.gamma_subject=objectVector[i].name;
					//Object is a pin
					relationStruct.gamma_object=objectVector[j].name;
					relationStruct.nameRelation=NAMERELATION;
					relationStruct.degreeRelation=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, CONNECTED_THRESHOLD);
					/*degree=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, CONNECTED_THRESHOLD);
					relationDegree=(int)(degree*1000.0)/1000.0;
					relationStruct.degreeRelation=relationDegree;*/
					relationStructVector.push_back(relationStruct);
				}

				if(objectVector[j].name.find("t")!=std::string::npos){
					//cout<<"\n";
					//cout<<"Entra in t-otherObject "<<"\n";
					//cout<<"Subject: "<< objectVector[i].name<<"\n";
					//cout<<"Object: "<< objectVector[j].name<<"\n";

				  	//Subject
					relationStruct.gamma_subject=objectVector[i].name;
					//Object is the table
					relationStruct.gamma_object=objectVector[j].name;
					relationStruct.nameRelation=NAMERELATION;
					relationStruct.degreeRelation=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					/*degree=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					relationDegree=(int)(degree*1000.0)/1000.0;
					relationStruct.degreeRelation=relationDegree;*/
					relationStructVector.push_back(relationStruct);
				}

				if(objectVector[i].name.find("p")!=std::string::npos && objectVector[j].name.find("p")!=std::string::npos){
					//cout<<"\n";
					//cout<<"Entra in p-p "<<"\n";
					//cout<<"Subject: "<< objectVector[i].name<<"\n";
					//cout<<"Object: "<< objectVector[j].name<<"\n";

					//Subject is a leg
					relationStruct.gamma_subject=objectVector[i].name;
					//Object is a pin
					relationStruct.gamma_object=objectVector[j].name;
					relationStruct.nameRelation=NAMERELATION;
					relationStruct.degreeRelation=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					/*degree=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					relationDegree=(int)(degree*1000.0)/1000.0;
					relationStruct.degreeRelation=relationDegree;*/
					relationStructVector.push_back(relationStruct);
				}

				if(objectVector[i].name.find("g")!=std::string::npos && objectVector[j].name.find("g")!=std::string::npos){
					cout<<"\n";
					//cout<<"Entra in g-g "<<"\n";
					//cout<<"Subject: "<< objectVector[i].name<<"\n";
					//cout<<"Object: "<< objectVector[j].name<<"\n";
					//Subject is a leg
					relationStruct.gamma_subject=objectVector[i].name;
					//Object is a pin
					relationStruct.gamma_object=objectVector[j].name;
					relationStruct.nameRelation=NAMERELATION;
					relationStruct.degreeRelation=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					/*degree=computeRelation(objectVector[i].x, objectVector[i].y, objectVector[j].x, objectVector[j].y, 0.4);
					relationDegree=(int)(degree*1000.0)/1000.0;
					relationStruct.degreeRelation=relationDegree;*/
					relationStructVector.push_back(relationStruct);
				}

			}
		}
	}

}



//LA NUOVA COMPUTELEGPINRELATION IN ORIGINE ERA: double computeLegPinRelation (double xy [2], double p[ROWS][COLUMNS], int pin, std::string name, std::string leg)

//NUOVA COMPUTELEGPINRELATION
/*double computeLegPinRelation (double xy [2], double p[ROWS][COLUMNS], int pin){
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
    /*for (int i = 0; i<COLUMNS; i++){ 
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
}*/


//NUOVA COMPUTEPINTABLERELATION
/*double computePinTableRelation(double p[ROWS][COLUMNS], int pin){
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
}*/







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
void callback_save_image (const sensor_msgs::ImageConstPtr& msg){
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
}




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
int eval_pin (double xy [2], double p[ROWS][COLUMNS], std::string name, std::string leg, object &leg_object, object &pin_object, int gamma_index){

    std::string pin_gammaName="p_"+ boost::to_string(gamma_index);
    pin_object.name=pin_gammaName;
   
    double x=xy[0];
    double y=xy[1];
    cout<<"\n";
    //cout<<"X ENTRA NELLA FUNZIONE: "<< x<<"\n";
    //cout<<"Y ENTRA NELLA FUNZIONE: "<<y<<"\n";

    if (name == "NOT_X" || name == "BED_X")
        x=x-0.115;
    else if (name == "NOT_MINUS_X" || name == "BED_MINUS_X")
        x=x+0.115;
    else if (name == "NOT_Y" || name == "BED_Y")
        y=y-0.115;
    else if (name == "NOT_MINUS_Y" || name == "BED_MINUS_Y")
        y=y+0.115;
    
    leg_object.x=x;
    leg_object.y=y;
   
    
   // cout<<"LEG X ESCE DAGLI IF: "<<x<<"\n";
    //cout<<"LEG Y ESCE DAGLI IF: "<<y<<"\n";


    //cout<<"NOME LEG IN STRUCT NELLA EVAL_PIN: " <<leg_object.name<<"\n";
    //cout<<"VALORE LEG X IN STRUCT NELLA EVAL_PIN: " <<leg_object.x<<"\n";
    //cout<<"VALORE LEG Y IN STRUCT NELLA EVAL_PIN: " <<leg_object.y<<"\n";
    


    
    int pin=0;
    for (int i = 0; i<COLUMNS; i++){
        if (x<p[1][i]+THR && x>p[1][i]-THR){
            if (y<p[2][i]+THR && y>p[2][i]-THR){
                pin = i+1;
               //cout<<"PIN TROVATO: " <<pin <<"\n";
                //sceneStruct_Leg.type_pin="Pin_"+ boost::to_string(pin);
                //cout<<"PIN ENTRA NELLA FUNZIONE: "<<sceneStruct_Leg.type_pin<<"\n";
                 pin_object.x=p[1][i];
		 pin_object.y=p[2][i];
		 //cout<<"\n";
		 //cout<<"PIN NAME IN STRUCT DENTRO EVAL_PIN: "<< pin_object.name<<"\n";
		 //cout<<"PIN X IN STRUCT DENTRO EVAL_PIN: "<< pin_object.x<<"\n";
		 //cout<<"PIN Y IN STRUCT DENTRO EVAL_PIN: "<< pin_object.y<<"\n";

                 /////////////////////////////
		 //fill an item of type pin
		 itemStruct.gamma_i=pin_gammaName;		 
		 itemStruct.type="Pin_"+boost::to_string(pin);
		 itemStruct.degree=1;
                 //put the item in a vector of items
		 itemStructVector.push_back(itemStruct);
		 ////////////////////////////////////

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



void eval_config (double angles[3],tf::StampedTransform t, double xy[2], double pins[ROWS][COLUMNS], configuration &conf_leg, std::string leg_name, object &leg_object, object &pin_object, vector<object> &objectVector){
	
    map<string,double> legsMap;
    tf::Matrix3x3 m0(t.getRotation());
    m0.getEulerYPR(angles[2], angles[1], angles[0]);

    //roll radiants to degrees
    angles[0] = angles[0] * (180 / M_PI);
    //pitch radiants to degrees
    angles[1] = angles[1] * (180 / M_PI);
    //yaw radiants to degrees
    angles[2] = angles[2] * (180 / M_PI);

    change_angle_interval(angles);

    gamma_index++;
    std::string leg_gammaName="g_"+ boost::to_string(gamma_index);
    leg_object.name=leg_gammaName;

    //After "check_configuration" function I know the type of the leg and the orientation respect to the WORLD frame
    legsMap=check_configuration(angles, conf_leg, leg_name);
	
    
    
	
    //sceneStruct_Leg.type_leg=conf_leg.name_config;
    
    ROS_INFO("Conf leg %s:  %s", leg_name.c_str(), conf_leg.name_config.c_str());
   // cout<<"TIPO DI LEG NELLA STRUCT: " << sceneStruct_Leg.type_leg <<"\n";
    xy[0]=t.getOrigin().x();
    xy[1]=t.getOrigin().y();
    int pin_type=eval_pin(xy, pins, conf_leg.name_config, conf_leg.leg_id, leg_object, pin_object, gamma_index);
    conf_leg.pin=pin_type;

    //cout<<"\n";
    //cout<<"LEG NAME IN STRUCT FUORI EVAL_PIN: "<< leg_object.name<<"\n";
    //cout<<"LEG X IN STRUCT FUORI EVAL_PIN: "<< leg_object.x<<"\n";
    //cout<<"LEG Y IN STRUCT FUORI EVAL_PIN: "<< leg_object.y<<"\n";
    //cout<<"PIN NAME IN STRUCT FUORI EVAL_PIN: "<< pin_object.name<<"\n";
    //cout<<"PIN X IN STRUCT FUORI EVAL_PIN: "<< pin_object.x<<"\n";
    //cout<<"PIN Y IN STRUCT FUORI EVAL_PIN: "<< pin_object.y<<"\n";

    //cout<< "pin type: " << conf_leg.pin<<"\n";
    
    //cout << "leg before the check on pin type: " << leg_object.name << "\n";
    //cout << "pin before the check on pin type: " << pin_object.name << "\n";
    if(pin_type!=0){
	   // cout << "\n";
	    //cout << "leg insert in the vector of objects and as item: " << leg_object.name<<"\n";
	    //cout << "pin insert in the vector of objects and as item: " << pin_object.name << "\n";
	    objectVector.push_back(leg_object);
	    objectVector.push_back(pin_object);

//////////////////////////////MAP CONTENT IS PUT IN THE ITEMS ARRAY EVEN SAME GAMMA WITH DIFFERENT TYPES
	    for (map<string,double>::iterator it=legsMap.begin(); it!=legsMap.end(); ++it){
		    itemStruct.gamma_i=leg_gammaName;
		    itemStruct.type=it->first;
		    itemStruct.degree=it->second;
		    itemStructVector.push_back(itemStruct);
	    }
    }
    
    //every time a new leg is evaluated at the end of computations the map with type and degree will be cleared
    legsMap.clear();

    
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
    ros::Subscriber sub = n.subscribe("kinect2/qhd/image_mono", 1000, callback_save_image);


    




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

    struct object leg_object;
    struct object pin_object;
    struct object table_object;
    vector<object> objectStructVector;
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
        ros::Duration(1.0).sleep();
        tf::StampedTransform transform_w_100;
        tf::StampedTransform transform_w_104;
        tf::StampedTransform transform_w_108;
        tf::StampedTransform transform_w_112;
        vision::SceneTable::Ptr ourScene (new vision::SceneTable);


       


 //sit_armor_injected_msgs::SceneElementVector::Ptr sceneForArmor(new sit_armor_injected_msgs::SceneElementVector);


/////////////////////////Piece of code to take the image from the bag file convert it to cv_bridge andsave it/////////////////////////	
	

	//string name_image= path_to_save_images+to_string(frameInstant)+".jpg";
	frameInstant++;

	n.getParam("/folder_name", folder_name);
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
	if (inputImage){
	string frame = boost::lexical_cast<string>(frameInstant);
	
	string name_image= dirname + "_" + frame + ".png";
	
	imwrite(name_image, inputImage->image);
	inputImage.reset();
	}
	cout<<"\n";
	ROS_INFO("\nTHE FRAME NOW IS: %d", frameInstant);
	//cout<<"\n";

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
                        
            eval_config(angles_100,transform_w_100, xy_100, pins, conf_leg0, "Leg_0", leg_object, pin_object, objectStructVector);

	    
            if (conf_leg0.pin > 0 && conf_leg0.name_config.size()>0) {
                f << conf_leg0.leg_id << std::endl << conf_leg0.name_config << std::endl << "Pin:" << conf_leg0.pin << std::endl << std::endl;
                fypr << angles_100[2] <<" " << angles_100[1] <<" " << angles_100[0] << std::endl;
                fxy << "leg 0 : " << xy_100[0] <<" " << xy_100[1] << std:: endl;

		

                vision::Configuration::Ptr msg0(new vision::Configuration);

                init_original_message(ourScene, msg0, conf_leg0);

///////////////////////////////////////////////////////////////////////////////////////////////
                
		
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
            eval_config(angles_104,transform_w_104, xy_104, pins, conf_leg4, "Leg_4",leg_object,pin_object, objectStructVector);

            if (conf_leg4.pin > 0 && conf_leg4.name_config.size()>0){
                f << conf_leg4.leg_id << std::endl << conf_leg4.name_config << std::endl << "Pin:" << conf_leg4.pin << std::endl << std::endl;
                fypr << angles_104[2] <<" " << angles_104[1] <<" " << angles_104[0] << std::endl;
                fxy << "leg 4: " << xy_104[0] <<" " << xy_104[1] << std:: endl;


                vision::Configuration::Ptr msg4(new vision::Configuration);

                
		init_original_message(ourScene, msg4, conf_leg4);



///////////////////////////////////////////////////////////////////////////////////////////////
                
		
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

            eval_config(angles_108,transform_w_108, xy_108, pins, conf_leg8, "Leg_8", leg_object, pin_object, objectStructVector);

            if (conf_leg8.pin > 0 && conf_leg8.name_config.size()>0){
                f << conf_leg8.leg_id << std::endl << conf_leg8.name_config << std::endl << "Pin:" << conf_leg8.pin << std::endl << std::endl;
                fypr << angles_108[2] <<" " << angles_108[1] <<" " << angles_108[0] << std::endl;
                fxy << "leg 8: " << xy_108[0] <<" " << xy_108[1] << std:: endl;
		
		leg_counter++;
                vision::Configuration::Ptr msg8(new vision::Configuration);

                
		init_original_message(ourScene, msg8, conf_leg8);

///////////////////////////////////////////////////////////////////////////////////////////////
                

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

            eval_config(angles_112,transform_w_112, xy_112, pins, conf_leg12, "Leg_12", leg_object, pin_object, objectStructVector);

            if (conf_leg12.pin > 0 && conf_leg12.name_config.size()>0) {
                f << conf_leg12.leg_id << std::endl << conf_leg12.name_config << std::endl <<"Pin:" << conf_leg12.pin << std::endl << std::endl;
                fypr << angles_112[2] <<" " << angles_112[1] <<" " << angles_112[0] << std::endl;
                fxy << "leg 12: " << xy_112[0] <<" " << xy_112[1] << std:: endl;


                vision::Configuration::Ptr msg12(new vision::Configuration);

                init_original_message(ourScene, msg12, conf_leg12);

///////////////////////////////////////////////////////////////////////////////////////////////
                

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
	
	

	

	table_object.name="t";
	table_object.x=0;
	table_object.y=0;
	objectStructVector.push_back(table_object);


	/*for(vector<object>::iterator it = objectStructVector.begin(); it != objectStructVector.end(); ++it){

		
			cout<<"\n";
			cout<<"FUORI NEL MAIN"<<"\n";
			cout<<"SIZE ARRAY NOW: " << objectStructVector.size()<<"\n";
			cout<<"NOME GAMMA NELL'ARRAY: "<<it->name<<"\n";
		        
			cout<<"X LEG NELL'ARRAY: "<<it->x<<"\n";
		        cout<<"Y LEG NELL'ARRAY: "<<it->y<<"\n";
			
			cout<<"\n";
	  
		
	
	}*/

	itemStruct.gamma_i="t";
	itemStruct.type="Table";
	itemStruct.degree=1;
	itemStructVector.push_back(itemStruct);

	/*for(vector<item>::iterator it = itemStructVector.begin(); it != itemStructVector.end(); ++it){

		
			cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"VECTOR OF ITEMS"<<"\n";
			cout<<"SIZE ARRAY NOW: " << itemStructVector.size()<<"\n";
			cout<<"GAMMA NELL'ARRAY DI ITEMS: "<<it->gamma_i<<"\n";		        
			cout<<"TYPE NELL'ARRAY DI ITEMS: "<<it->type<<"\n";
		        cout<<"DEGREE NELL'ARRAY DI ITEMS: "<<it->degree<<"\n";
			
			cout<<"\n";
	  
		
	
	}*/
	
	computeAllRelations(objectStructVector);

	/*for(vector<relation>::iterator it_rel = relationStructVector.begin(); it_rel != relationStructVector.end(); ++it_rel){

		
			cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"VECTOR OF RELATIONS"<<"\n";
			cout<<"SIZE ARRAY NOW: " << itemStructVector.size()<<"\n";			
			cout<<"GAMMA SUBJECT: "<<it_rel->gamma_subject<<"\n";
			cout<<"GAMMA OBJECT: "<<it_rel->gamma_object<<"\n";	
			cout<<"NAME RELATION: "<<it_rel->nameRelation<<"\n";	        
		        cout<<"DEGREE RELATION: "<<it_rel->degreeRelation<<"\n";
			
			cout<<"\n";
	  
		
	
	}*/

	//Now create the scene object and the object for the items and the relations 
	vision::SceneToSIT::Ptr ourSceneToSIT (new vision::SceneToSIT);
	vision::Configuration_SIT::Ptr msgItemSIT(new vision::Configuration_SIT);
	vision::Relations::Ptr msgRelationSIT(new vision::Relations);
	init_SIT_message(ourSceneToSIT, msgItemSIT, msgRelationSIT, itemStructVector, relationStructVector);

	//computeRelations(ourSceneToSIT, pins, sceneStructVector, relation, relationInScene);
	

	objectStructVector.clear();
       
	
	
	//computeRelations(ourSceneToSIT);
	

        Scene_pub.publish(ourScene);
	SceneSIT_pub.publish(ourSceneToSIT);

	itemStructVector.clear();
	relationStructVector.clear();

        f << "-------------------"<< std::endl << k << std::endl;
        k++;
	
        ros::spinOnce();
        
	
	rate.sleep();
    }
    return 0;
}

