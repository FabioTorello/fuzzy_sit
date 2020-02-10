//
// Created by lorenzo on 17.07.19.
//
#include "ros/ros.h"
#include <tf/transform_listener.h>
#include <math.h>
#include <stdlib.h>
#include <map> 
#include <vector>

#ifndef VISION_LEGELABORATION_H
#define VISION_LEGELABORATION_H

#endif //VISION_LEGELABORATION_H
using namespace std;

vector<std::string> legOrientationsVector;
vector<std::string> legTypesVector;
vector<double> legDegreeOrientationsVector;
vector<double> legDegreeTypesVector;

struct configuration{

    std::string leg_id;
    std::string name_config;
    //double degreeOrientation;
    int pin;
   
    
    

};

struct item{

   std::string gamma_i;
   std::string type;
   double degree;       

};

struct relation{

   std::string gamma_subject;
   std::string gamma_object;
   std::string nameRelation;
   double degreeRelation;
    
    

};



struct object{
std::string name;
double x, y;
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
   /* cout<<"ROLL CHE ENTRA NELLA FUNZIONE: " << rpy[0] << "\n";
    cout<<"PITCH CHE ENTRA NELLA FUNZIONE: " << rpy[1] << "\n";
    cout<<"YAW CHE ENTRA NELLA FUNZIONE: " << rpy[2] << "\n";
    cout<<"FORMULA ENTRA NELLA FUNZIONE SETTYPE: " << formula << "\n";*/
    ///////////////////////////////// SETTO IN BED E NOT <226 E NON < 225 Perchè 225 NON VIENE MAI CONSIDERATO /////////////

    //////135 NON VIENE MAI CONSIDERATO
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
else if ( ((136 <= rpy[1]) && (rpy[1] < 226))&&((136 <= rpy[0]) && (rpy[0] < 226))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="NOT";
    }
    /*else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((136 <= rpy[0]) && (rpy[0] < 225))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="NOT";
    }*/
else if ( ((136 <= rpy[1]) && (rpy[1] < 226))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="BED";
    }
    /*else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="BED";
    }*/
else if ( ((136 <= rpy[0]) && (rpy[0] < 226))&&((315 <= rpy[1]) || (rpy[1] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="BED";
    }
    /*else if ( ((136 <= rpy[0]) && (rpy[0] < 225))&&((315 <= rpy[1]) || (rpy[1] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="BED";
    }*/
    //cout<<"FORMULA CHE ESCE DALLA SETTYPE: " <<formula <<"\n";
    return type;
}




std::string setOrientation (double rpy[3], double formula){

    cout<<"FORMULA IN setORIENTATION: "<<formula<<"\n";
    std::string orientation;

    if(formula < 0){
	formula=formula+360;

    }

    while(formula>360){
        formula=formula-360;
    }
    /*if (formula > 360){
        formula=formula-360;
    }*/

    if(formula > 315 || formula <= 45){

        orientation = "_X";

    }

    else if(formula > 45 && formula <= 135){

        orientation = "_Y";

    }

    else if(formula > 135 && formula <= 225){
        orientation = "_MINUS_X";

    }

    else if(formula > 225 && formula <= 315){

        orientation = "_MINUS_Y";

    }

    return orientation;
}



double setDegreeOrientation (double formula){

    double degree;
    cout<< "\n";
    //cout<<"FORMULA CHE ENTRA NELLA FUNZIONE: " << formula << "\n";
    //From negative angles to positive angles
    if(formula < 0){
	formula=formula+360;

    }

    //cout<<"FORMULA CHE ENTRA NELLA FUNZIONE: " << formula << "\n";
    while(formula>360){
        formula=formula-360;
    }

    /*if (formula > 360){
        formula=formula-360;
    }*/

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
    if (degree>1){
	ROS_ERROR("ERROR: Orientation degree is positive (degree: %f)!!!!!!!!!!!!!!!!!!\n", degree);
    }
   
    //config.degreeOrientation=degree;
    //cout<< "\n" << degree << "\n";
    return degree;
}





map<string, double> setFuzzyOrientation(double formula){
   cout<<"\n";
   cout<<"FORMULA: "<< formula<<"\n";
  //Map used to save the orientations and the related degrees
   map<std::string, double> orientations_degreeMap;
   std::string orientationLeg;
   double degreeOrientationLeg;

   //If formula is a negative angle it is converted to positive one by adding 360 degree
   if(formula < 0){
	formula=formula+360;

    }

     //If formula is greater than 360 degree it is computed the equivalent angle within the first 360 degrees
     while(formula>360){
        formula=formula-360;
    }

    /*if (formula > 360){
        formula=formula-360;
    }*/


   
   //Only _X
   if((formula>=325 && formula<=360) || (formula>=0 && formula<=35)){
	   cout<<"Only _X"<<"\n";
	   if(formula>=325 && formula<=360){
           	orientationLeg="_X";
  		degreeOrientationLeg=(((formula-325)/35)*0.8)+0.2;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }
	   else{
		orientationLeg="_X";
		degreeOrientationLeg=(((35-formula)/35)*0.8)+0.2;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }

   }

   //Only _Y
   if(formula>=55 && formula<=125){
	   cout<<"Only _Y"<<"\n";
	   if(formula>=55 && formula<=90){
           	orientationLeg="_Y";
  		degreeOrientationLeg=(((formula-55)/35)*0.8)+0.2;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }
	   else{
		orientationLeg="_Y";
		degreeOrientationLeg=(((125-formula)/35)*0.8)+0.2;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }

   }

   //Only _MINUS_X
   if(formula>=145 && formula<=215){
	   cout<<"Only _MINUS_X"<<"\n";
	   if(formula>=145 && formula<=180){
           	orientationLeg="_MINUS_X";
  		degreeOrientationLeg=(((formula-145)/35)*0.8)+0.2;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }
	   else{
		orientationLeg="_MINUS_X";
		degreeOrientationLeg=(((215-formula)/35)*0.8)+0.2;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }

   }

   //Only _MINUS_Y
   if(formula>=235 && formula<=305){
	   cout<<"Only _MINUS_Y"<<"\n";
	   if(formula>=235 && formula<=270){
           	orientationLeg="_MINUS_Y";
  		degreeOrientationLeg=(((formula-235)/35)*0.8)+0.2;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }
	   else{
		orientationLeg="_MINUS_Y";
		degreeOrientationLeg=(((305-formula)/35)*0.8)+0.2;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   }

   }

   // _X and _Y
   if(formula>35 && formula<55){

	        cout<<" _X And _Y"<<"\n";
           	orientationLeg="_X";
  		degreeOrientationLeg=(((45-formula)/10)*0.1)+0.1;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   
	  
		orientationLeg="_Y";
		degreeOrientationLeg=(((formula-45)/10)*0.1)+0.1;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	  

   }

   //_Y and _MINUS_X
   if(formula>125 && formula<145){

	  	cout<<" _Y And _MINUS_X"<<"\n";
           	orientationLeg="_Y";
  		degreeOrientationLeg=(((135-formula)/10)*0.1)+0.1;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   
	  
		orientationLeg="_MINUS_X";
		degreeOrientationLeg=(((formula-135)/10)*0.1)+0.1;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	  

   }

   //_MINUS_X and _MINUS_Y
   if(formula>215 && formula<235){

	  	cout<<" _MINUS_X And _MINUS_Y"<<"\n";
           	orientationLeg="_MINUS_X";
  		degreeOrientationLeg=(((225-formula)/10)*0.1)+0.1;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   
	  
		orientationLeg="_MINUS_Y";
		degreeOrientationLeg=(((formula-225)/10)*0.1)+0.1;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	  

   }

   //_MINUS_Y and _X
   if(formula>305 && formula<325){

	  	cout<<" _MINUS_Y And _X"<<"\n";
           	orientationLeg="_MINUS_Y";
  		degreeOrientationLeg=(((315-formula)/10)*0.1)+0.1;
                orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	   
	  
		orientationLeg="_X";
		degreeOrientationLeg=(((formula-315)/10)*0.1)+0.1;
		orientations_degreeMap.insert(pair<string,double>(orientationLeg,degreeOrientationLeg));
	  

   }

   return orientations_degreeMap;

}

map<string, double> check_configuration(double rpy[3], struct configuration &config, const std::string &leg_id){

    map<string, double> configuration_leg_degreeMap;
    map<string, double> orientation_leg_degreeMap;
    map<string, double> types_leg_degreeMap;

    vector<std::string> orientationsVector;
    vector<double> degreeOrientationVector;

    double formula;
    double degreeOrientation;
    double degree;
    std::string orientation; 

 
    std::string type=setType(rpy, formula);
    orientation=setOrientation(rpy, formula);

    /*degree=setDegreeOrientation(formula);
    degreeOrientation=(int)(degree*1000.0)/1000.0;*/

    degreeOrientation=setDegreeOrientation(formula);
    config.leg_id = leg_id;



    orientation_leg_degreeMap=setFuzzyOrientation(formula);

    for (map<string,double>::iterator it=orientation_leg_degreeMap.begin(); it!=orientation_leg_degreeMap.end(); ++it){
	orientationsVector.push_back(type+it->first);
        degreeOrientationVector.push_back(it->second);
    }
    
    
    std::string leg_type=type + orientation;
    
    //types_leg_degreeMap.
    configuration_leg_degreeMap.insert(pair<string,double>(leg_type,degreeOrientation));
    config.name_config= leg_type;
    //config.degreeOrientation=degreeOrientation;

    for(vector<std::string>::iterator it_orient = orientationsVector.begin(); it_orient != orientationsVector.end(); ++it_orient){

		
			cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"LEG CONFIGURATION"<<"\n";
			cout<<"LEG CONFIGURATION: " << *it_orient<<"\n";
			
			cout<<"\n";
	  
		
	
	}

 for(vector<double>::iterator it_degreeOrient = degreeOrientationVector.begin(); it_degreeOrient != degreeOrientationVector.end(); ++it_degreeOrient){

		
			cout<<"\n";
			cout<<"\n";
			cout<<"----------------------"<<"\n";
			cout<<"LEG DEGREE ORIENTATION"<<"\n";
			cout<<"DEGREE ORIENTATION: " << *it_degreeOrient<<"\n";
			
			cout<<"\n";
	  
		
	
	}
    
    orientationsVector.clear();
    degreeOrientationVector.clear();
    orientation_leg_degreeMap.clear();
  
    return configuration_leg_degreeMap;
    

}
