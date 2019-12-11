%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Extract data for statistics analysis or for studying
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all 
close all

%%Acquire Excel file in a table -- Dataset
Dataset = readtable('test1.2.xlsx');
[new_Dataset] = check_marker(Dataset);
%writetable(new_Dataset,'new_test1.2.xlsx');
