import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Patient {
    private final String medicalRecordNumber;
    private final String name;

    Patient(String medicalRecordNumber, String name) {
        this.medicalRecordNumber = medicalRecordNumber != null ? medicalRecordNumber.trim() : "";
        this.name = name != null ? name.trim() : "";
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "病歷號='" + medicalRecordNumber + '\'' +
                ", 姓名='" + name + '\'' +
                '}';
    }
}

public class ClinicQueueSystem {
    private final List<Patient> waitingList = new ArrayList<>();
    private final List<Patient> completedList = new ArrayList<>();

    public void register(Patient patient) {
        if (patient == null || patient.getMedicalRecordNumber().isEmpty()) {
            return;
        }
        waitingList.add(patient);
        System.out.println("掛號成功：" + patient);
    }

    public boolean cancel(String medicalRecordNumber) {
        if (medicalRecordNumber == null || medicalRecordNumber.trim().isEmpty()) {
            return false;
        }
        String cleanMrn = medicalRecordNumber.trim();
        Iterator<Patient> iterator = waitingList.iterator();
        while (iterator.hasNext()) {
            Patient p = iterator.next();
            if (p.getMedicalRecordNumber().equals(cleanMrn)) {
                iterator.remove();
                System.out.println("取消掛號成功：病歷號 " + cleanMrn + " (" + p.getName() + ") 已退出隊列");
                return true;
            }
        }
        System.out.println("取消掛號失敗：找不到病歷號為 " + cleanMrn + " 的病患");
        return false;
    }

    public Patient callNext() {
        if (waitingList.isEmpty()) {
            System.out.println("叫號提示：目前無候診病患。");
            return null;
        }
        // 移除最前端的病患 (FIFO)
        Patient nextPatient = waitingList.remove(0);
        completedList.add(nextPatient);
        System.out.println("請診號：" + nextPatient + " 前往診間看診");
        return nextPatient;
    }

    public Patient peekNext() {
        if (waitingList.isEmpty()) {
            return null;
        }
        return waitingList.get(0);
    }

    public List<Patient> getWaitingList() {
        return new ArrayList<>(waitingList);
    }

    public List<Patient> getCompletedList() {
        return new ArrayList<>(completedList);
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業二：診所掛號系統 ===");

        ClinicQueueSystem clinic = new ClinicQueueSystem();

        // 1. 掛號測試
        System.out.println("--- 開始掛號 ---");
        clinic.register(new Patient("P001", "Amy"));
        clinic.register(new Patient("P002", "Ben"));
        clinic.register(new Patient("P003", "Cara"));
        clinic.register(new Patient("P004", "David"));

        System.out.println("目前候診人數：" + clinic.getWaitingList().size());
        System.out.println("目前候診名單：" + clinic.getWaitingList());

        // 2. 查看下一位
        System.out.println("\n查看下一位候診者：" + clinic.peekNext()); // Amy

        // 3. 叫號看診
        System.out.println("\n--- 開始叫號看診 ---");
        clinic.callNext(); // Amy
        clinic.callNext(); // Ben

        // 4. 取消掛號測試
        System.out.println("\n--- 取消掛號與異常取消測試 ---");
        clinic.cancel("P003"); // 取消 Cara，應成功
        clinic.cancel("P099"); // 取消不存在的病歷，應印出失敗提示

        System.out.println("取消後目前候診名單：" + clinic.getWaitingList()); // 應只剩 David (P004)

        // 5. 繼續叫號
        System.out.println("\n--- 繼續叫號看診 ---");
        clinic.callNext(); // David
        clinic.callNext(); // 候診已空，應提示無人

        // 6. 印出當日完成清單
        System.out.println("\n--- 當日完成看診病患清單 ---");
        System.out.println(clinic.getCompletedList()); // 應包含 Amy, Ben, David (不含 Cara，因為她取消了)
    }
}
