package sugelico.postabsugelico.General.Definitions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RuleList  implements java.io.Serializable, Find{
    public RuleList(){

    }
    private ArrayList<Rules> rules;

    public ArrayList<Rules> getRules() {
        return rules;
    }

    public void setRules(JSONArray rulesjson) throws JSONException {
        rules = new ArrayList<Rules>() ;
        for (int cont=0; cont<rulesjson.length(); cont++){
            Rules rule=new Rules();
            rule.setCode(rulesjson.getJSONObject(cont).getInt("rule_code"));
            rule.setName(rulesjson.getJSONObject(cont).getString("rule_name"));
            rules.add(rule);
        }
    }
    public boolean findRule(Integer code){
        for (Rules rule :rules){
            if (rule.getCode().equals(code)){
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean contains(Object value) {
        return null;
    }
}
