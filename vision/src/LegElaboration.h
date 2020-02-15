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
std::string prev_type_leg;

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

/*double* turnToValidType(double rpy[3]){

 double rpy_modified[3];
 double distance;

 rpy_modified[0]=rpy[0];
 if(prev_type_leg=="ROOF"){
   if(rpy[1]<45){
     d
   }
   if(rpy[1]<135){
   }
  
 }
 else if(prev_type_leg=="CHAIR"){

 }
 else if(prev_type_leg=="NOT"){

 }
 else if(prev_type_leg=="BED"){

 }

 rpy_modified[1]=rpy[1];
 rpy_modified[2]=rpy[2];

 return rpy_modified;
}*/



std::string setType(const double rpy[3], double &formula){

    std::string type;
    cout<<"ROLL CHE ENTRA NELLA FUNZIONE: " << rpy[0] << "\n";
    cout<<"PITCH CHE ENTRA NELLA FUNZIONE: " << rpy[1] << "\n";
    //cout<<"YAW CHE ENTRA NELLA FUNZIONE: " << rpy[2] << "\n";
    //cout<<"FORMULA ENTRA NELLA FUNZIONE SETTYPE: " << formula << "\n";
    ///////////////////////////////// SETTO IN BED E NOT <226 E NON < 225 Perchè 225 NON VIENE MAI CONSIDERATO /////////////

    //////135 NON VIENE MAI CONSIDERATO
    if( ((45 <= rpy[1]) && (rpy[1] < 136))  ){ // p = 90° --> roof

        formula = rpy[2] - rpy[0];
        type="ROOF";
	//prev_type_leg=type;
    }
	


	/*if( (45 <= rpy[1]) && (rpy[1] < 135)){ // p = 90° --> roof

        formula = rpy[2] - rpy[0];
        type="ROOF";
    }*/
    
    else if ( ((226 <= rpy[1]) && (rpy[1] < 315)) ){ // p = 90° --> roof

        formula = rpy[2] + rpy[0] + 180;
        type="CHAIR";
	//prev_type_leg=type;
    }

    else if ( ((315 <= rpy[1]) || (rpy[1] < 45))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="NOT";
	//prev_type_leg=type;
    }

    else if ( ((136 <= rpy[1]) && (rpy[1] < 226))&&((136 <= rpy[0]) && (rpy[0] < 226))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="NOT";
 	//prev_type_leg=type;
    }

    /*else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((136 <= rpy[0]) && (rpy[0] < 225))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="NOT";
    }*/

    else if ( ((136 <= rpy[1]) && (rpy[1] < 226))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="BED";
	//prev_type_leg=type;
    }

    /*else if ( ((136 <= rpy[1]) && (rpy[1] < 225))&&((315 <= rpy[0]) || (rpy[0] < 45))){ // p = 90° --> roof

        formula = rpy[2] + 180;
        type="BED";
    }*/

    else if ( ((136 <= rpy[0]) && (rpy[0] < 226))&&((315 <= rpy[1]) || (rpy[1] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="BED";
	//prev_type_leg=type;
    }

    /*else if ( ((136 <= rpy[0]) && (rpy[0] < 225))&&((315 <= rpy[1]) || (rpy[1] < 45))){ // p = 90° --> roof

        formula = rpy[2];
        type="BED";
    }*/
    //If type is empty it is assumed equal to the previous leg type and compute the formula by using a recursive call function
    

   /* if (type.empty()){
      if((45 <= rpy[0]) && (rpy[0] < 136)){
	formula = rpy[2] - rpy[0];
        type="ROOF";
	}
      if(((226 <= rpy[0]) && (rpy[0] < 315))){
	formula = rpy[2] + rpy[0] + 180;
        type="CHAIR";
      }
    }*/


    /*if (type.empty())
    {
	rpy=turnToValidType(rpy);
	type=setType(rpy,formula);
    }*/
    //cout<<"FORMULA CHE ESCE DALLA SETTYPE: " <<formula <<"\n";
    return type;
}




std::string setOrientation (double rpy[3], double formula){

    //cout<<"FORMULA IN setORIENTATION: "<<formula<<"\n";
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

    //cout<< "FORMULA DOPO CAMBIO SE > 360 GRADI: " << formula << "\n";

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




//FUZZY ORIENTATION FUNCTION
map<string, double> setFuzzyOrientation(double formula, map<string, double> &types_degreeMap, map<string,double> &configuration_Map){
   cout<<"\n";
   cout<<"FORMULA: "<< formula <<"\n";

   //Map used to save the combination between the types and the orientations
   //map<string, double> configuration_Map;
  //Map used to save the orientations and the related degrees
   map<std::string, double> orientations_degreeMap;
   std::string orientationLeg;
   double degreeOrientationLeg;

   //Variables describe the configuration which leg is and the related degree
   double degreeLeg;
   std::string configurationLeg;

   //If formula is a negative angle it is converted to positive one by adding 360 degree
   if(formula < 0){
	formula=formula+360;

    }

     //If formula is greater than 360 degree it is computed the equivalent angle within the first 360 degrees
     while(formula>360){
        formula=formula-360;
    }

    


   
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


//Combination between elements of two maps (types and orientations)
  for (map<string,double>::iterator typesMap_it=types_degreeMap.begin(); typesMap_it!=types_degreeMap.end(); ++typesMap_it){



	    for (map<string,double>::iterator orientMap_it=orientations_degreeMap.begin(); orientMap_it!=orientations_degreeMap.end(); ++orientMap_it){

		configurationLeg=(typesMap_it->first)+(orientMap_it->first);

		degreeLeg=std::min(typesMap_it->second,orientMap_it->second);

		configuration_Map.insert(pair<string,double>(configurationLeg,degreeLeg));
	    }




   }

   
   orientations_degreeMap.clear();

   return configuration_Map;

}







//FUZZY TYPE FUNCTION
map<string, double> setFuzzyType(double rpy[3], double formula){

   cout<<"ROLL CHE ENTRA NELLA FUNZIONE: " << rpy[0] << "\n";
    cout<<"PITCH CHE ENTRA NELLA FUNZIONE: " << rpy[1] << "\n";
//cout<<"YAW CHE ENTRA NELLA FUNZIONE: " << rpy[2] << "\n";
cout<< "\n";
   //Map used to save the types and the related degrees
   map<std::string, double> types_degreeMap;

   //Map used to save the configuration map after setFuzzyOrientation function operations. This is returned from setFuzzyType function
   map<string, double> configuration_leg_FuzzyMap;

  //Map gives as reference to setOrientation function used for the SetFuzzyOrientation function operations in particular when the types are more than one so the returned map could loose information
   map<string, double> configuration_degreeMap;

   std::string typeLeg;
   double degreeTypeLeg;
 

   //If the angles (yaw-pitch-roll) are negative they are converted to positive ones by adding 360 degree
   if(rpy[0] < 0){
	rpy[0]=rpy[0]+360;

    }
    
    if(rpy[1] < 0){
	rpy[1]=rpy[1]+360;

    }
    
     if(rpy[2] < 0){
	rpy[2]=rpy[2]+360;

    }

     //If formula is greater than 360 degree it is computed the equivalent angle within the first 360 degrees
     while(rpy[0]>360){
        rpy[0]=rpy[0]-360;
    }

    while(rpy[1]>360){
        rpy[1]=rpy[1]-360;
    }
    
    while(rpy[2]>360){
        rpy[2]=rpy[2]-360;
    }




   //Only BED/NOT (1)
   if((rpy[1]>=325 && rpy[1]<=360) || (rpy[1]>=0 && rpy[1]<=35)){

	   cout<<"Only BED/NOT between 325 and 35 degrees"<<"\n";

	   if(rpy[1]>=325 && rpy[1]<=360){

		//NOT
		if((315 <= rpy[0]) || (rpy[0] < 45)) {  
 
     			cout<<"Only NOT between 325 and 35 degrees"<<"\n";
			typeLeg="NOT";
			formula=rpy[2];
	  		degreeTypeLeg=(((rpy[1]-325)/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}

		//BED
		if((136 <= rpy[0]) && (rpy[0] < 226)) {  
 
     			cout<<"Only BED between 325 and 35 degrees"<<"\n";
			typeLeg="BED";
			formula=rpy[2];
	  		degreeTypeLeg=(((rpy[1]-325)/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}
	   }

	   else{

		//NOT
		if((315 <= rpy[0]) || (rpy[0] < 45)) {  
 
     			cout<<"Only NOT between 325 and 35 degrees"<<"\n";
			typeLeg="NOT";
			formula=rpy[2];
	  		degreeTypeLeg=(((35-rpy[1])/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}
		
		//BED
		if((136 <= rpy[0]) && (rpy[0] < 226)) { 
  
     			cout<<"Only BED between 325 and 35 degrees"<<"\n";
			typeLeg="BED";
			formula=rpy[2];
	  		degreeTypeLeg=(((35-rpy[1])/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}
		
	   }

   }

   //Only ROOF
   if(rpy[1]>=55 && rpy[1]<=125){

	   cout<<"Only ROOF"<<"\n";

	   if(rpy[1]>=55 && rpy[1]<=90){

           	typeLeg="ROOF";
		formula = rpy[2]-rpy[0];
  		degreeTypeLeg=(((rpy[1]-55)/35)*0.8)+0.2;
                types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

	   }

	   else{

		typeLeg="ROOF";
		formula = rpy[2]-rpy[0];
		degreeTypeLeg=(((125-rpy[1])/35)*0.8)+0.2;
		types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

	   }

   }

   //Only BED/NOT (2)
   if(rpy[1]>=145 && rpy[1]<=215){

	   cout<<"Only BED/NOT between 145 and 215"<<"\n";

	   if(rpy[1]>=145 && rpy[1]<=180){

		//Only NOT
		if((136 <= rpy[0]) && (rpy[0] < 226)){

			cout<<"Only NOT between 145 and 215"<<"\n";
		   	typeLeg="NOT";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((rpy[1]-145)/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

		//Only BED
		if((315 <= rpy[0]) || (rpy[0] < 45)){

			cout<<"Only BED between 145 and 215"<<"\n";
		   	typeLeg="BED";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((rpy[1]-145)/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

	   }
	   else{
		
		//Only NOT
		if((136 <= rpy[0]) && (rpy[0] < 226)){

			cout<<"Only NOT between 145 and 215"<<"\n";
		   	typeLeg="NOT";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((215-rpy[1])/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

		//Only BED
		if((315 <= rpy[0]) || (rpy[0] < 45)){

			cout<<"Only BED between 145 and 215"<<"\n";
		   	typeLeg="BED";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((215-rpy[1])/35)*0.8)+0.2;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

	   }

   }

   //Only CHAIR
   if(rpy[1]>=235 && rpy[1]<=305){

	   cout<<"Only CHAIR"<<"\n";
	   if(rpy[1]>=235 && rpy[1]<=270){

           	typeLeg="CHAIR";
		formula = rpy[2]+rpy[0]+180;
  		degreeTypeLeg=(((rpy[1]-235)/35)*0.8)+0.2;
                types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

	   }

	   else{

		typeLeg="CHAIR";
 		formula = rpy[2]+rpy[0]+180;
		degreeTypeLeg=(((305-rpy[1])/35)*0.8)+0.2;
		types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

	   }

   }


   // BED/NOT and ROOF (1)
   if(rpy[1]>35 && rpy[1]<55){

	        cout<<"BED/NOT and ROOF between 35 and 55"<<"\n";

           	typeLeg="ROOF";
		formula=rpy[2]-rpy[0];
  		degreeTypeLeg=(((rpy[1]-45)/10)*0.1)+0.1;
                types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
	   	
	  	//Now we have to determine which is the type between BED and NOT

		//NOT
		if((315 <= rpy[0]) || (rpy[0] < 45)) {  
 
     			cout<<"NOT between 35 and 55 degrees"<<"\n";
			typeLeg="NOT";
			formula=rpy[2];
	  		degreeTypeLeg=(((45-rpy[1])/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}

		//BED
		if((136 <= rpy[0]) && (rpy[0] < 226)) {  
 
     			cout<<"BED between 35 and 55 degrees"<<"\n";
			typeLeg="BED";
			formula=rpy[2];
	  		degreeTypeLeg=(((45-rpy[1])/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}
		
	  	

   }

   //BED/NOT and ROOF (2)
   if(rpy[1]>125 && rpy[1]<145){

	  	cout<<"BED/NOT and ROOF between 125 and 145"<<"\n";
           	typeLeg="ROOF";
		formula=rpy[2]-rpy[0];
  		degreeTypeLeg=(((135-rpy[1])/10)*0.1)+0.1;
                types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

	   	//Now we have to determine which is the type between BED and NOT

		//NOT
		if((136 <= rpy[0]) && (rpy[0] < 226)){

			cout<<"NOT between 125 and 145 degrees"<<"\n";
		   	typeLeg="NOT";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((rpy[1]-135)/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

		//BED
		if((315 <= rpy[0]) || (rpy[0] < 45)){

			cout<<"BED between 125 and 145 degrees"<<"\n";
		   	typeLeg="BED";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((rpy[1]-135)/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}
	  	
		

   }

   //BED/NOT and CHAIR (3)
   if(rpy[1]>215 && rpy[1]<235){

	  	cout<<"BED/NOT and CHAIR between 215 and 235"<<"\n"; 	
	   
		typeLeg="CHAIR";
		formula=rpy[2]+rpy[0]+180;
		degreeTypeLeg=(((rpy[1]-225)/10)*0.1)+0.1;
		types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		//Now we have to determine which is the type between BED and NOT

		//NOT
		if((136 <= rpy[0]) && (rpy[0] < 226)){

			cout<<"NOT between 215 and 235 degrees"<<"\n";
		   	typeLeg="NOT";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((225-rpy[1])/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}

		//BED
		if((315 <= rpy[0]) || (rpy[0] < 45)){

			cout<<"BED between 215 and 235 degrees"<<"\n";
		   	typeLeg="BED";
			formula=rpy[2]+180;
	  		degreeTypeLeg=(((225-rpy[1])/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
		}
	  
			  

   }

   //BED/NOT and CHAIR (4)
   if(rpy[1]>305 && rpy[1]<325){

	  	cout<<"BED/NOT and CHAIR between 305 and 325"<<"\n";

           	typeLeg="CHAIR";
		formula=rpy[2]+rpy[0]+180;
  		degreeTypeLeg=(((315-rpy[1])/10)*0.1)+0.1;
                types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

		configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);
	   
	  	//Now we have to determine which is the type between BED and NOT

		//NOT
		if((315 <= rpy[0]) || (rpy[0] < 45)) {  
 
     			cout<<"NOT between 305 and 325 degrees"<<"\n";
			typeLeg="NOT";
			formula=rpy[2];
	  		degreeTypeLeg=(((rpy[1]-315)/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}

		//BED
		if((136 <= rpy[0]) && (rpy[0] < 226)) {  
 
     			cout<<"BED between 305 and 325 degrees"<<"\n";
			typeLeg="BED";
			formula=rpy[2];
	  		degreeTypeLeg=(((rpy[1]-315)/10)*0.1)+0.1;
		        types_degreeMap.insert(pair<string,double>(typeLeg,degreeTypeLeg));

			configuration_leg_FuzzyMap=setFuzzyOrientation(formula, types_degreeMap, configuration_degreeMap);

		}


   }
  
   types_degreeMap.clear();
   configuration_degreeMap.clear();
   return configuration_leg_FuzzyMap;

}









map<string, double> check_configuration(double rpy[3], struct configuration &config, const std::string &leg_id){

    map<string, double> configuration_leg_degreeMap;
    //map<string, double> orientations_leg_degreeMap;
    //map<string, double> types_leg_degreeMap;

    //vector<std::string> orientationsVector;
    //vector<double> degreeOrientationVector;

    double formula;
    double formula_crisp;
   // double degreeOrientation;    
    std::string orientation; 

    
	

 
    //std::string type=setType(rpy, formula_crisp);
    //orientation=setOrientation(rpy, formula_crisp);
    config.leg_id = leg_id;
    //std::string leg_type=type + orientation;
    //config.name_config= leg_type;

    /*degree=setDegreeOrientation(formula);
    degreeOrientation=(int)(degree*1000.0)/1000.0;*/

    //degreeOrientation=setDegreeOrientation(formula);

    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FUZZY FUNCTIONS AND COMPUTATIONS

    configuration_leg_degreeMap=setFuzzyType(rpy, formula);
   // orientations_leg_degreeMap=setFuzzyOrientation(formula);

    

    //configuration_leg_degreeMap.insert(pair<string,double>(leg_type,degreeOrientation));
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
   /*for (map<string,double>::iterator it=orientation_leg_degreeMap.begin(); it!=orientation_leg_degreeMap.end(); ++it){
	orientationsVector.push_back(type+it->first);
        degreeOrientationVector.push_back(it->second);
    }*/
    
   
    
    
    //config.degreeOrientation=degreeOrientation;

    /*for(vector<std::string>::iterator it_orient = orientationsVector.begin(); it_orient != orientationsVector.end(); ++it_orient){

		
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
	  
		
	
	}*/
    
   // orientationsVector.clear();
   // degreeOrientationVector.clear();
    //types_leg_degreeMap.clear();
    //orientations_leg_degreeMap.clear();
  
    return configuration_leg_degreeMap;
    

}
