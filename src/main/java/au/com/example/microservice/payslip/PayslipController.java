package au.com.example.microservice.payslip;

import au.com.example.microservice.constant.Constant;
import au.com.example.microservice.payslip.exception.UnsupportedFileTypeException;
import au.com.example.microservice.payslip.model.IncomeSummariesWrapper;
import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummariesWrapper;
import au.com.example.microservice.payslip.model.PayslipSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@Validated
@RequestMapping(value = "/payslip")
public class PayslipController {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipController.class);

  @Autowired
  private PayslipService payslipService;

  @Autowired
  private PayslipFileService payslipFileService;

  @Autowired
  private MessageSource messageSource;

  @RequestMapping(value = "/monthly/summary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PayslipSummary> payslipSummary(@Valid @RequestBody IncomeSummary incomeSummary) {

    final PayslipSummary payslip = payslipService.calculate(incomeSummary);

    return new ResponseEntity<>(payslip, HttpStatus.OK);
  }

  @RequestMapping(value = "/monthly/summaries", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PayslipSummariesWrapper> payslipSummaries(@Valid @RequestBody IncomeSummariesWrapper wrapper) {

    final List<PayslipSummary> payslipSummaries = new ArrayList<>();

    wrapper.getIncomeSummaries().forEach(incomeSummary -> {
      payslipSummaries.add(payslipService.calculate(incomeSummary));
    });

    return new ResponseEntity<>(new PayslipSummariesWrapper(payslipSummaries), HttpStatus.OK);
  }

  @RequestMapping(value = "/monthly/summaries/batch", method = RequestMethod.POST)
  public ResponseEntity<Resource> payslipSummariesBatch(@RequestParam("file") MultipartFile file) {

    if (payslipFileService.isSupportedFileType(file.getContentType())) {

      final List<PayslipSummary> payslipSummaries = new ArrayList<>();

      final List<IncomeSummary> incomeSummaries = payslipFileService.convert(file);

      LOG.debug("Number of income summaries {}", incomeSummaries.size());

      incomeSummaries.forEach(incomeSummary -> {
        payslipSummaries.add(payslipService.calculate(incomeSummary));
      });

      final Resource resource = payslipFileService.convert(payslipSummaries);

      return ResponseEntity
              .ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"summary.csv\"")
              .header(HttpHeaders.CONTENT_TYPE, Constant.TEXT_CSV)
              .body(resource);
    }

    throw new UnsupportedFileTypeException(
            messageSource.getMessage("error.filetype.unsupported", null, Locale.getDefault()));
  }
}
