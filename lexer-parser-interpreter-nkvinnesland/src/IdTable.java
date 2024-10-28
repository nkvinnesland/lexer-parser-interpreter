import java.util.HashMap;
import java.util.Objects;

public class IdTable {
    private HashMap<String, Integer> idMap;

    public IdTable(){
        idMap = new HashMap<>();
    }

    public void addEntry(String id){
        if (!idMap.containsKey(id)){
            int address = idMap.size();
            idMap.put(id, address);
        }
    }

    public int getAddress(String id){
        if (idMap.containsKey((id))){
            return idMap.get(id);
        } else {
            return -1;
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String id : idMap.keySet()){
            sb.append(id).append(": ").append(idMap.get(id)).append("\n");
        }
        return "ID Table: \n" + sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdTable idTable = (IdTable) o;
        return Objects.equals(idMap, idTable.idMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idMap);
    }
}
