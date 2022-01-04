package jp.cloudnativedays.social.analysis.service;


import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MorphologicalAnalysis {

    public List<Token> getToken(String in){
        Tokenizer tokenizer = new Tokenizer();
        return tokenizer.tokenize(in);
    }
}
