package ch.so.agi.simi.core.copy;

import com.haulmont.cuba.core.Persistence;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service(CopyService.NAME)
public class CopyServiceBean implements CopyService {

    @Override
    public void copyProduct(UUID id){
        //load dataproduct

        //copy
    }

    private void copyDataSetView(UUID id){

    }

}