function [new_Dataset] = check_marker(Dataset)
 rows = size(Dataset,1);
 columns = size(Dataset,2);
 
%  table={'H';'B';'C';'S'}; elem=[1;5;6;16];  % build lookup table

%new_column = table(ones(rows,1), 'VariableNames',{'Table_Part'});
new_column = table('Size',[rows 1],'VariableTypes',{'string'},'VariableNames',{'Table_Part'});
column_five=Dataset(:,5);


% find any row with 'WORLD'
ix=any(ismember(column_five{:,1},{'WORLD'}),2);
new_column(ix,1)={'WORLD'}; 

% find any row with 'WORLD_CENTER_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_16'}),2);
new_column(ix,1)={'WORLD_CENTER_REAL_MARKER'};

% find any row with 'WORLD_CENTER_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_42'}),2);
new_column(ix,1)={'WORLD_CENTER_FICTIVE_MARKER'};

% find any row with 'WORLD_X_REAL_MARKER' 
ix=any(ismember(column_five{:,1},{'ar_marker_17'}),2);
new_column(ix,1)={'WORLD_X_REAL_MARKER'};

% find any row with 'WORLD_X_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_43'}),2);
new_column(ix,1)={'WORLD_X_FICTIVE_MARKER'};  

% find any row with 'WORLD_Y_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_18'}),2);
new_column(ix,1)={'WORLD_Y_REAL_MARKER'};

% find any row with 'WORLD_Y_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_44'}),2);
new_column(ix,1)={'WORLD_Y_FICTIVE_MARKER'}; 

% find any row with 'LEG_0_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_100'}),2);
new_column(ix,1)={'LEG_0_FICTIVE_MARKER'}; 

% find any row with 'LEG_0_LONG_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_0';'ar_marker_1';'ar_marker_2';'ar_marker_3'}),2);
new_column(ix,1)={'LEG_0_LONG_BAR_REAL_MARKER'};

% find any row with 'LEG_0_SHORT_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_24';'ar_marker_25';'ar_marker_26';'ar_marker_27'}),2);
new_column(ix,1)={'LEG_0_SHORT_BAR_REAL_MARKER'};

% find any row with 'LEG_4_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_104'}),2);
new_column(ix,1)={'LEG_4_FICTIVE_MARKER'};

% find any row with 'LEG_4_LONG_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_4';'ar_marker_5';'ar_marker_6';'ar_marker_7'}),2);
new_column(ix,1)={'LEG_4_LONG_BAR_REAL_MARKER'};

% find any row with 'LEG_4_SHORT_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_33';'ar_marker_34';'ar_marker_35';'ar_marker_36'}),2);
new_column(ix,1)={'LEG_4_SHORT_BAR_REAL_MARKER'};

% find any row with 'LEG_8_FICTIVE_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_108'}),2);
new_column(ix,1)={'LEG_8_FICTIVE_MARKER'};

% find any row with 'LEG_8_LONG_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_8';'ar_marker_9';'ar_marker_10';'ar_marker_11'}),2);
new_column(ix,1)={'LEG_8_LONG_BAR_REAL_MARKER'};

% find any row with 'LEG_8_SHORT_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_19';'ar_marker_20';'ar_marker_21';'ar_marker_22'}),2);
new_column(ix,1)={'LEG_8_SHORT_BAR_REAL_MARKER'};

% find any row with 'LEG_12_FICTIVE_MARKER' 
ix=any(ismember(column_five{:,1},{'ar_marker_112'}),2);
new_column(ix,1)={'LEG_12_FICTIVE_MARKER'};

% find any row with 'LEG_12_LONG_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_12';'ar_marker_13';'ar_marker_14';'ar_marker_15'}),2);
new_column(ix,1)={'LEG_12_LONG_BAR_REAL_MARKER'};

% find any row with 'LEG_12_SHORT_BAR_REAL_MARKER'
ix=any(ismember(column_five{:,1},{'ar_marker_37';'ar_marker_38';'ar_marker_39';'ar_marker_40'}),2);
new_column(ix,1)={'LEG_12_SHORT_BAR_REAL_MARKER'};

%  for i=1:rows
%      %%if statements to check the strings in the fifth column of the table
%      if (strcmp(column_five(i,1)=='ar_marker_16'))
%          new_column(i,1)={'WORLD_CENTER_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_42'))             
%          new_column(i,1)={'WORLD_CENTER_FICTIVE_MARKER'};
%         
%      elseif (strcmp(column_five(i,1),'ar_marker_17'))
%          new_column(i,1)={'WORLD_X_CENTER_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_43'))            
%          new_column(i,1)={'WORLD_X_CENTER_FICTIVE_MARKER'};
%         
%      elseif (strcmp(column_five(i,1),'ar_marker_18'))
%          new_column(i,1)={'WORLD_Y_CENTER_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_44'))             
%          new_column(i,1)={'WORLD_Y_CENTER_FICTIVE_MARKER'};
%      
%      elseif (strcmp(column_five(i,1),'ar_marker_100'))
%          new_column(i,1)={'LEG_0_FICTIVE_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_0')||...
%              strcmp(column_five(i,1),'ar_marker_1')||...
%              strcmp(column_five(i,1),'ar_marker_2')||...
%              strcmp(column_five(i,1),'ar_marker_3'))             
%          new_column(i,1)={'LEG_0_LONG_BAR_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_24')||...
%              strcmp(column_five(i,1),'ar_marker_25')||...
%              strcmp(column_five(i,1),'ar_marker_26')||...
%              strcmp(column_five(i,1),'ar_marker_27'))             
%          new_column(i,1)={'LEG_0_SHORT_BAR_REAL_MARKER'};
%          
%      elseif (strcmp(column_five(i,1),'ar_marker_104'))
%          new_column(i,1)={'LEG_4_FICTIVE_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_4')||...
%              strcmp(column_five(i,1),'ar_marker_5')||...
%              strcmp(column_five(i,1),'ar_marker_6')||...
%              strcmp(column_five(i,1),'ar_marker_7'))             
%          new_column(i,1)={'LEG_4_LONG_BAR_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_33')||...
%              strcmp(column_five(i,1),'ar_marker_34')||...
%              strcmp(column_five(i,1),'ar_marker_35')||...
%              strcmp(column_five(i,1),'ar_marker_36'))             
%          new_column(i,1)='LEG_4_SHORT_BAR_REAL_MARKER';
%          
%      elseif (strcmp(column_five(i,1),'ar_marker_108'))
%          new_column(i,1)={'LEG_8_FICTIVE_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_8')||...
%              strcmp(column_five(i,1),'ar_marker_9')||...
%              strcmp(column_five(i,1),'ar_marker_10')||...
%              strcmp(column_five(i,1),'ar_marker_11'))             
%          new_column(i,1)={'LEG_8_LONG_BAR_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_19')||...
%              strcmp(column_five(i,1),'ar_marker_20')||...
%              strcmp(column_five(i,1),'ar_marker_21')||...
%              strcmp(column_five(i,1),'ar_marker_22'))             
%          new_column(i,1)={'LEG_8_SHORT_BAR_REAL_MARKER'};
%          
%      elseif (strcmp(column_five(i,1),'ar_marker_112'))
%          new_column(i,1)={'LEG_12_FICTIVE_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_12')||...
%              strcmp(column_five(i,1),'ar_marker_13')||...
%              strcmp(column_five(i,1),'ar_marker_14')||...
%              strcmp(column_five(i,1),'ar_marker_15'))             
%          new_column(i,1)={'LEG_12_LONG_BAR_REAL_MARKER'};
%      elseif (strcmp(column_five(i,1),'ar_marker_37')||...
%              strcmp(column_five(i,1),'ar_marker_38')||...
%              strcmp(column_five(i,1),'ar_marker_39')||...
%              strcmp(column_five(i,1),'ar_marker_40'))             
%          new_column(i,1)={'LEG_12_SHORT_BAR_REAL_MARKER'};
%          
%      else
%          new_column(i,1)={'WORLD'};
%      end
%  end 

 new_Dataset=horzcat(Dataset(:,1:5),new_column,Dataset(:,6:12));
end