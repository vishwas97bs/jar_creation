package preprocessors.Impl;


import constants.Constants;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveSpacesInBetweenAmountPreProcessor implements TextPreprocessor {

    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName) {
        String preProcessedText = input;
        String regexString="((?<=[[^\\S\\r\\n]|{currencies}|,|.]{1,3})\\d+([.|,|[^\\S\\r\\n]]{1,5}\\d+)+)".replace(Constants.REGEX_POS_CURRENCIES,helper.getCurrenciesRegex());
        Pattern pattern=Pattern.compile(regexString);

        Matcher m=pattern.matcher(preProcessedText);
        StringBuffer sb =new StringBuffer();
        while(m.find())
        {
            String matchedString=m.group(0).replaceAll("\\s{1,}"," ");
            StringBuffer sbMatchedString=new StringBuffer();
            if(matchedString.length()>11)
            {
                m.appendReplacement(sb,matchedString);

                continue;
            }


            String[] matchStringSplitBySpace= matchedString.split(" ");
            int decimalCount=0;
            for(String eachSplit:matchStringSplitBySpace)
            {
                if(eachSplit.contains("."))
                    decimalCount++;
            }

            if(matchStringSplitBySpace.length>2 & !(matchedString.contains(".")  |  matchedString.contains(",")))
            {
                m.appendReplacement(sb,matchedString);
                continue;
            }

            if(decimalCount==matchStringSplitBySpace.length)
            {
                m.appendReplacement(sb,matchedString);
                continue;
            }
            int index=0;
            while(matchedString.length()>index)
            {
                if(matchedString.charAt(index)==' ' & matchedString.length()-1>index) {
                    if (matchedString.charAt(index + 1) == ',' || matchedString.charAt(index + 1) == '.') {
                        sbMatchedString.append(matchedString.charAt(index + 1));
                        index++;
                        if(matchedString.length()-1>index) {
                            if (matchedString.charAt(index + 1) == ' ' || matchedString.charAt(index + 1) == '.' || matchedString.charAt(index + 1) == ',')
                                index++;
                        }

                    } else {
                        sbMatchedString.append(',');
                    }
                }
                else if(matchedString.charAt(index)=='.'||matchedString.charAt(index)==',')
                {
                    sbMatchedString.append(matchedString.charAt(index));
                    while(matchedString.length()>index+1 )
                    {
                        if(matchedString.charAt(index+1)==' '||matchedString.charAt(index+1)==','||matchedString.charAt(index+1)=='.')
                            index++;
                        else
                            break;
                    }
                }
                else{
                    sbMatchedString.append(matchedString.charAt(index));
                }
                index++;
            }
            m.appendReplacement(sb,sbMatchedString.toString().trim());
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
