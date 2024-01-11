package com.mkyong.xml.jaxb;

import com.mkyong.xml.jaxb.model.Company;
import com.mkyong.xml.jaxb.model.Staff;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JaxbExample1 {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            //jaxbContext = JAXBContext.newInstance(Company.class);

            // EclipseLink MOXy needs jaxb.properties at the same package with Company.class or Staff.class
            // Alternative, I prefer define this via eclipse JAXBContextFactory manually.
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[] {Company.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // XML Unmarshalling
            File file = new File("C:\\test\\file.xml");
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Company o = (Company) jaxbUnmarshaller.unmarshal(file);
            //System.out.println(o);

            jaxbMarshaller.marshal(createCompanyObjectList(o), new File("C:\\test\\fileList.xml"));

            //jaxbMarshaller.marshal(createCompanyObject(), System.out);



        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private static Company createCompanyObject(Company input) {

        Company outCompany = new Company();
        outCompany.setName(input.getName());

        Staff inputStaff= input.getList().get(0);

        Staff staff1 = new Staff(inputStaff);
        inputStaff.setName("changed name");

        /*Staff o1 = new Staff();
        o1.setId(1);
        o1.setName("mkyong");
        o1.setSalary("8000 & Bonus");
        o1.setBio("<h1>support</h1>");
        o1.setJoinDate(ZonedDateTime.now().minusMonths(12));

        Staff o2 = new Staff();
        o2.setId(2);
        o2.setName("yflow");
        o2.setSalary("9000");
        o2.setBio("<h1>developer & database</h1>");
        o2.setJoinDate(ZonedDateTime.now().minusMonths(6));*/

        outCompany.setList(Arrays.asList(inputStaff, staff1));

        return outCompany;
    }

    private static Company createCompanyObjectList(Company input) {

        Company outCompany = new Company();

        Staff inputStaff= input.getList().get(0);

        List<Staff> staffList = new ArrayList<>();
        for(int i=0;i<100;i++){
            Staff staff = new Staff(inputStaff);
            staff.setName("Name "+i);
            staff.setId(i);
            staffList.add(staff);
        }

        outCompany.setList(staffList);

        return outCompany;
    }
}
