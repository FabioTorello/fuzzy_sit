//
// Created by lorenzo on 17.07.19.
//
#include "ros/ros.h"
#include <tf/transform_listener.h>
#include <math.h>
#include <stdlib.h>
#ifndef VISION_LEGELABORATION_H
#define VISION_LEGELABORATION_H

#endif //VISION_LEGELABORATION_H
using namespace std;


struct configuration{

    std::string leg_id;
    std::string name_config;
    double degreeOrientation;
    int pin;
    std::string nameRelation;
    double pinTableRelationDegree;
    double legPinRelationDegree;
    
    

};

void change_angle_interval(double rpy[3]){

    for(int i=0; i<3; i++){

        if(rpy[i]<0)
            rpy[i]=rpy[i]+360;

    }

}

/*void compute_sum(double sum[3], const tf::StampedTransform &t){

    tf::Matrix3x3 m(t.getRotation());
    double roll, pitch, yaw;
    m.getEulerYPR(yaw, pitch, roll);

    //ROS_INFO("%f", yaw);
    //ROS_INFO("%f", roll);

    sum[0] = (roll + sum[0]);
    sum[1]= (pitch + sum[1]);
    sum[2] = (yaw + sum[2]);


}

void compute_avg(double avg[], const double sum[], int k, int size){

    for (int i = 0; i<size; i++){
        avg[i]=sum[i]/k;
    }
//    avg[0]=sum[0]/i;
//    avg[1]=sum[1]/i;
//    avg[2]=sum[2]/i;
}*/

std::string setType(const double rpy[3], double &formula){

    std::string type;
    if( (45 <= rpy[1]) && (rpy[1] < 135)){ // p = 90° --> roof

        formula = rpy[2] - rpy[0];
        type="ROOF";
    }
    else if ( (226 <= rpy[1]) && (rpy[1] < 315)){ // p = 90° --> roof

        formula = rpy[2] + rpy[0] + 180;
        type="CHAIR";
    }
    else if ( ((315 <= rpy[1]) || (rpy[1] < 45))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="NOT";
    }
    else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((136 <= rpy[0]) && (rpy[0] < 225))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="NOT";
    }
    else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="BED";
    }
    else if ( ((136 <= rpy[0]) && (rpy[0] < 225))&&((315 <= rpy[1]) || (rpy[1] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="BED";
    }

    return type;
}

std::string setOrientation (double rpy[3], double formula){

    std::string orientation;
    if (formula > 360){
        formula=formula-360;
    }
    if(formula > 315 || formula < 45){

        orientation = "_X";

    }

    else if(formula > 45 && formula < 135){

        orientation = "_Y";

    }

    else if(formula > 135 && formula < 225){
        orientation = "_MINUS_X";

    }

    else if(formula > 225 && formula < 315){

        orientation = "_MINUS_Y";

    }

    return orientation;
}

double setDegreeOrientation (double formula){

    double degree;
    cout<<"FORMULA CHE ENTRA NELLA FUNZIONE: " << formula << "\n";

    if (formula > 360){
        formula=formula-360;
    }

    cout<< "FORMULA DOPO CAMBIO SE > 360 GRADI: " << formula << "\n";

    //All if for degree = 1
    if (formula == 0 || formula==360){
	//orientation = "_X";
	degree=1.0;
    }
    else if(formula==90){
	//orientation = "_Y";
	degree=1.0;
    }
    else if(formula==180){
	//orientation = "_MINUS_X";
	degree=1.0;
    }
    else if(formula==270){
	//orientation = "_MINUS_Y";
	degree=1.0;
    }
   
    //All if for degree = 0
    if (formula==315){
	//orientation = "_X";
	degree=0;
    }
    else if(formula==45){
	//orientation = "_Y";
	degree=0;
    }
    else if(formula==135){
	//orientation = "_MINUS_X";
	degree=0;
    }
    else if(formula==225){
	//orientation = "_MINUS_Y";
	degree=0;
    }


    //All if statements to compute the degree
    if (formula>315 && formula<360){
	//orientation = "_X";
    	//degree=((315-formula)/315);
	degree=((formula-315)/45);
	//ROS_INFO("\n ENTRA NEL > 315 && < 360");
    }
    else if (formula>0 && formula<45){
	//orientation = "_X";
    	degree=((45-formula)/45);
        //ROS_INFO("\n ENTRA NEL > 0 && < 45");
    }
    else if(formula>45 && formula<90){
	//orientation = "_Y";
    	degree=((formula-45)/45);
	//ROS_INFO("\n ENTRA NEL > 45 && < 90");
    }
    else if(formula>90 && formula<135){
	//orientation = "_Y";
    	degree=((135-formula)/45);
	//ROS_INFO("\n ENTRA NEL > 90 && < 135");
    }
    else if(formula>135 && formula<180){
	//orientation = "_MINUS_X";
	degree=((formula-135)/45);
	//ROS_INFO("\n ENTRA NEL > 135 && < 180");
    }
    else if(formula>180 && formula<225){
	//orientation = "_MINUS_X";
	degree=((225-formula)/45);
	//ROS_INFO("\n ENTRA NEL > 180 && < 225");
    }
    else if(formula>225 && formula<270){
	//orientation = "_MINUS_Y";
	degree=((formula-225)/45);
	//ROS_INFO("\n ENTRA NEL > 225 && < 270");
    }
    else if(formula>270 && formula<315){
	//orientation = "_MINUS_Y";
	degree=((315-formula)/45);
	//ROS_INFO("\n ENTRA NEL > 270 && < 315 ");
    }
    else{
  	//orientation="  ";
        degree=0;
    }
    /*if(formula > 315 || formula < 45){

        orientation = "_X";

    }

    else if(formula > 45 && formula < 135){

        orientation = "_Y";

    }

    else if(formula > 135 && formula < 225){
        orientation = "_MINUS_X";

    }

    else if(formula > 225 && formula < 315){

        orientation = "_MINUS_Y";

    }*/
    //config.degreeOrientation=degree;
    //cout<< "\n" << degree << "\n";
    return degree;
}

void check_configuration(double rpy[3], struct configuration &config, const std::string &leg_id){
    double formula;
    double degreeOrientation;
    std::string orientation;  
    std::string type=setType(rpy, formula);
    orientation=setOrientation(rpy, formula);
    degreeOrientation=setDegreeOrientation(formula);
    config.leg_id = leg_id;
    config.name_config= type + orientation;
    config.degreeOrientation=degreeOrientation;
    

}
