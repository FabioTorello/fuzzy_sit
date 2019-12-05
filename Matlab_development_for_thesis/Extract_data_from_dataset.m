%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Extract data for statistics analysis or for studying
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all 
close all

%%Acquire Excel file in a table -- Dataset
%Dataset = table2cell(readtable('test1.2.xlsx'));
Dataset = readtable('test1.2.xlsx');
[new_Dataset] = check_marker(Dataset);
