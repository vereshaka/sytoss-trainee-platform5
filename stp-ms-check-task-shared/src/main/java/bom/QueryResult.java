package bom;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class QueryResult {

    private final List<HashMap<String, Object>> resultMapList;

    public HashMap<String,Object> getRow(int index){
        return resultMapList.get(index);
    }
}
