package au.edu.sydney.soft3202.majorProject;

import au.edu.sydney.soft3202.majorProject.controller.mainPageController;
import au.edu.sydney.soft3202.majorProject.model.appSystem;
import au.edu.sydney.soft3202.majorProject.model.sqlModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class testMyProject  {
    private appSystem  mockAppSystem;
    private sqlModel mockSqlModel;
    private mainPageController controller;


    @BeforeEach
    public void setUp(){
        mockSqlModel = mock(sqlModel.class);
//        mockAppSystem = mock(appSystem.class);
        controller = new mainPageController();
    }

    @AfterEach
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void testDBSetUp(){
        controller.setupDataBase(mockSqlModel);
        verify(mockSqlModel,times(1)).createDB();
        verify(mockSqlModel,times(1)).setupDB();
    }

    @Test
    public void testAction(){

    }



}
