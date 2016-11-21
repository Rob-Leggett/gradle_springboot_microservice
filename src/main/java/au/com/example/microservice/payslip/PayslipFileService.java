package au.com.example.microservice.payslip;


import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 */
public interface PayslipFileService {

  boolean isSupportedFileType(String fileType);

  List<IncomeSummary> convert(MultipartFile file);

  Resource convert(List<PayslipSummary> payslipSummaries);
}
