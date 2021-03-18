package me.shaneslone.fairshare.services;

import me.shaneslone.fairshare.exceptions.ResourceNotFoundException;
import me.shaneslone.fairshare.models.Bill;
import me.shaneslone.fairshare.models.MonthlyBill;
import me.shaneslone.fairshare.repositories.MonthlyBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlyBillServiceImpl implements MonthlyBillService{
    @Autowired
    private MonthlyBillRepository monthlyBillRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private HelperFunctions helperFunctions;

    @Override
    public List<MonthlyBill> findAll() {
        List<MonthlyBill> monthlyBills = new ArrayList<>();
        monthlyBillRepository.findAll().iterator().forEachRemaining(monthlyBills::add);
        return monthlyBills;
    }

    @Override
    public MonthlyBill findByMonthlyBillId(long id) {
        return monthlyBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly Bill Id " + id + " not found!"));
    }

    @Override
    public void delete(long id) {
        monthlyBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly Bill Id " + id + " not found!"));
        monthlyBillRepository.deleteById(id);
    }

    @Override
    public MonthlyBill save(MonthlyBill monthlyBill) {
        MonthlyBill newMonthlyBill = new MonthlyBill();
        if(monthlyBill.getMonthlybillid() != 0){
            monthlyBillRepository.findById(monthlyBill.getMonthlybillid())
                    .orElseThrow(() -> new ResourceNotFoundException("Monthly Bill Id " + monthlyBill.getMonthlybillid() + " not found!"));
            newMonthlyBill.setMonthlybillid(monthlyBill.getMonthlybillid());
        }
        newMonthlyBill.setMonth(monthlyBill.getMonth());
        newMonthlyBill.setYear(monthlyBill.getYear());
        newMonthlyBill.setHousehold(monthlyBill.getHousehold());

        newMonthlyBill = monthlyBillRepository.save(newMonthlyBill);

        for(Bill b : monthlyBill.getBills()){
            b.setMonthlyBill(newMonthlyBill);
            b = billService.save(b);
            newMonthlyBill.getBills().add(b);
        }
        return monthlyBillRepository.save(newMonthlyBill);
    }

    @Override
    public MonthlyBill update(MonthlyBill monthlyBill, long id) {
        MonthlyBill currentMonthlyBIll = findByMonthlyBillId(id);
        if(helperFunctions.isHouseholdMember(currentMonthlyBIll.getHousehold().getUsers())){
            if(monthlyBill.getMonth() != null){
                currentMonthlyBIll.setMonth(monthlyBill.getMonth());
            }
            if(monthlyBill.getYear() > 0){
                currentMonthlyBIll.setYear(monthlyBill.getYear());
            }
            if(monthlyBill.getBills().size() > 0){
                currentMonthlyBIll.getBills().clear();
                for(Bill b: monthlyBill.getBills()){
                    b.setMonthlyBill(currentMonthlyBIll);
                    b = billService.save(b);
                    currentMonthlyBIll.getBills().add(b);
                }
            }
            if(monthlyBill.getHousehold() != null){
                currentMonthlyBIll.setHousehold(monthlyBill.getHousehold());
            }
            return monthlyBillRepository.save(currentMonthlyBIll);
        } else {
            throw new ResourceNotFoundException("User is not authorized to make this change!");
        }
    }

    @Override
    public void deleteAll() {
        monthlyBillRepository.deleteAll();
    }
}
