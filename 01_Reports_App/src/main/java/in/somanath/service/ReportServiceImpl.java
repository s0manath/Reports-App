package in.somanath.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.somanath.entity.CitizenPlan;
import in.somanath.repo.CitizenPlanRepository;
import in.somanath.request.SearchRequest;
import in.somanath.utils.EmailUtils;
import in.somanath.utils.ExcelGenerator;
import in.somanath.utils.PdfGenerator;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CitizenPlanRepository planrepo;

	@Autowired
	private ExcelGenerator excelgenerator;

	@Autowired
	private PdfGenerator pdfgenerator;

	@Autowired
	private EmailUtils emailutils;

	@Override
	public List<String> getPlanName() {
		return planrepo.getPlanName();
	}

	@Override
	public List<String> getPlanStatuses() {
		return planrepo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> search(SearchRequest request) {
		CitizenPlan entity = new CitizenPlan();
		if (null != request.getPlanName() && !"".equals(request.getPlanName())) {
			entity.setPlanName(request.getPlanName());
		}
		if (null != request.getPlanStatus() && !"".equals(request.getPlanStatus())) {
			entity.setPlanStatus(request.getPlanStatus());
		}
		if (null != request.getGender() && !"".equals(request.getGender())) {
			entity.setGender(request.getGender());
		}
		if (null != request.getStartDate() && !"".equals(request.getStartDate())) {
			String startDate = request.getStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(startDate, formatter);
			entity.setPlanStartDate(localDate);
		}
		if (null != request.getEndDate() && !"".equals(request.getEndDate())) {
			String endDate = request.getEndDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(endDate, formatter);
			entity.setPlanStartDate(localDate);
		}
		return planrepo.findAll(Example.of(entity));
	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception {

		File f = new File("Plans.xls");

		List<CitizenPlan> plans = planrepo.findAll();
		excelgenerator.generate(response, plans, f);

		String subject = "Test mail Subject";
		String body = "<h1>Test mail Body";
		String to = "somanathmohapatra45@gmail.com";

		emailutils.sendMail(subject, body, to, f);

		f.delete();

		return true;
	}

	@Override
	public boolean exportPdf(HttpServletResponse response) throws Exception {
		File f = new File("Plans.pdf");

		List<CitizenPlan> plans = planrepo.findAll();
		pdfgenerator.generator(response, plans, f);

		String subject = "Test mail Subject";
		String body = "<h1>Test mail Body";
		String to = "somanathmohapatra45@gmail.com";

		emailutils.sendMail(subject, body, to, f);

		f.delete();

		return true;
	}

}
