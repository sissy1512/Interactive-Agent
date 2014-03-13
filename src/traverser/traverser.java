package traverser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import parser.*;

public class traverser implements QueryParserVisitor {
	
	public queryData queryConditionsJob = new queryData();
	public queryData queryConditionsResume = new queryData();
	HashMap<String, String> asked;

	//public ArrayList<queryData> queryConditions = new ArrayList<queryData>();
	
	public traverser(HashMap<String, String> asked){
		this.asked = asked;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		System.err.println("you should not come here!");
		System.exit(1);
		return null;
	}

	@Override
	public Object visit(ASTStart node, Object data) {
		// TODO Auto-generated method stub
		SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
		firstChild.jjtAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTseek node, Object data) {
		// TODO Auto-generated method stub
		node.jjtGetChild(0).jjtAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTJobOr node, Object data) {
		// TODO Auto-generated method stub
//		node.jjtGetChild(0).jjtAccept(this, data);
//		node.jjtGetChild(1).jjtAccept(this, data);
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTJob node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTtitle node, Object data) {
		// TODO Auto-generated method stub
		//queryData qd = ((queryData)data).clone();
//		String path = qd.getPath();
//		qd.setPath(path + ".title");
//		qd.setValue(node.title);
//		queryConditionsJob.add(qd);
		String path = ((String)data).toLowerCase();
		queryConditionsJob.addCondition(path + ".title", node.title.toLowerCase());
		asked.put(path + ".title", node.title.toLowerCase());
		queryConditionsResume.keywords.add(node.title.toLowerCase());
		return null;
	}

	@Override
	public Object visit(ASTcompany node, Object data) {
		// TODO Auto-generated method stub
	//	queryData qd = ((queryData)data).clone();
		SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
		if(firstChild instanceof ASTpreferableCompany){
			SimpleNode secChild = (SimpleNode) node.jjtGetChild(1);
			if(secChild instanceof ASTCompanyNameOr || secChild instanceof ASTCompanyName){
				queryConditionsJob.setCompanyNamePrefer();
			} else{
				queryConditionsJob.setCompanyTypePrefer();
			}
			secChild.jjtAccept(this, data);
		} else{
			if(firstChild instanceof ASTCompanyNameOr || firstChild instanceof ASTCompanyName){
				queryConditionsJob.setCompanyNamePrefer();
			} else{
				queryConditionsJob.setCompanyTypePrefer();
			}
			firstChild.jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTpreferableCompany node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCompanyNameOr node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTCompanyName node, Object data) {
		// TODO Auto-generated method stub
		//Job
//		queryData qd1 = ((queryData)data).clone();
//		String path = qd1.getPath();
//		qd1.setPath(path + ".company_name");
//		qd1.setValue(node.companyName);
//		queryConditionsJob.add(qd1);
//		//Resume
//		queryData qd2 = ((queryData)data).clone();
//		path = qd2.getPath();
//		qd2.setPath(path + ".experience.organization_name");
//		qd2.setValue(node.companyName);
//		queryConditionsResume.add(qd2);
		String path = ((String)data).toLowerCase();
		queryConditionsJob.addCondition(path + ".company_name", node.companyName.toLowerCase());
		asked.put(path + ".company_name", node.companyName.toLowerCase());
//		queryConditionsResume.addCondition(path + ".experience.organization_name", node.companyName);
		queryConditionsResume.keywords.add(node.companyName.toLowerCase());
		return null;
	}

	@Override
	public Object visit(ASTCompanyTypeOr node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTCompnayType node, Object data) {
		// TODO Auto-generated method stub
//		queryData qd = ((queryData)data).clone();
//		String path = qd.getPath();
//		qd.setPath(path + ".discipline");
//		qd.setValue(node.companyType);
//		queryConditionsJob.add(qd);
		String path = ((String)data).toLowerCase();
		queryConditionsJob.addCondition(path + ".discipline", node.companyType.toLowerCase());
		asked.put(path + ".discipline", node.companyType.toLowerCase());
		queryConditionsResume.keywords.add(node.companyType.toLowerCase());
		return null;
	}

	@Override
	public Object visit(ASTarea node, Object data) {
		// TODO Auto-generated method stub
//		queryData qd = ((queryData)data).clone();
		SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
		if(firstChild instanceof ASTpreferableArea){
			SimpleNode secChild = (SimpleNode) node.jjtGetChild(1);
			queryConditionsJob.setAreaPrefer();
			secChild.jjtAccept(this, data);
		} else{
			firstChild.jjtAccept(this, data);
		}
		return null;
	}

	@Override
	public Object visit(ASTpreferableArea node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTinArea node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTaroundArea node, Object data) {
		// TODO Auto-generated method stub
		queryConditionsJob.setAreaPrefer();
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTAreaNameOr node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTAreaName node, Object data) {
		// TODO Auto-generated method stub
		//Job
//		queryData qd1 = ((queryData)data).clone();
//		String path = qd1.getPath();
//		qd1.setPath(path + ".location");
//		qd1.setValue(node.areaName);
//		queryConditionsJob.add(qd1);
		String path = ((String)data).toLowerCase();
		queryConditionsJob.addCondition(path + ".location", node.areaName.toLowerCase());
		asked.put(path + ".location", node.areaName.toLowerCase());
		//Resume
//		queryData qd2 = ((queryData)data).clone();
//		path = qd2.getPath();
//		qd2.setPath(path + ".location.city");
//		qd2.setValue(node.areaName);
//		queryConditionsResume.add(qd2);
//		queryConditionsResume.addCondition(path + ".location.city", node.areaName);
		queryConditionsResume.keywords.add(node.areaName.toLowerCase());
		return null;
	}

	@Override
	public Object visit(ASTsalary node, Object data) {
		// TODO Auto-generated method stub
//		queryData qd = ((queryData)data).clone();
//		String path = qd.getPath();
//		qd.setPath(path + ".compensation");
//		qd.setValue(node.salary);
//		queryConditionsJob.add(qd);
		String path = ((String)data).toLowerCase();
		queryConditionsJob.addCondition(path + ".compensation", node.salary.toLowerCase());
		asked.put(path + ".compensation", node.salary.toLowerCase());
		queryConditionsResume.keywords.add(node.salary.toLowerCase());
		return null;
	}
}
