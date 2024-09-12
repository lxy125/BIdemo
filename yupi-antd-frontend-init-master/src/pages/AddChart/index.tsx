import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Select, Space, Upload, Typography, Card } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { genChartByAiUsingPost } from '@/services/backend/chartController';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';
import './AddChart.css';  // 自定义样式文件

const { Title } = Typography;

const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  const [submitting, setSubmitting] = useState<boolean>(false);

  const onFinish = async (values: any) => {
    if (submitting) return;
    setSubmitting(true);
    setChart(undefined);
    setOption(undefined);

    const params = {
      ...values,
      file: undefined,
    };

    try {
      const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj);
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        const chartOption = JSON.parse(res.data.genChart ?? '');
        if (!chartOption) {
          throw new Error('解析失败');
        } else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="add-chart-container">
      <Card className="form-card" bordered={false}>
        <Title level={3} className="form-title">AI 图表分析生成</Title>
        <Form
          name="addChart"
          onFinish={onFinish}
          initialValues={{}}
          layout="vertical"
          className="form-layout"
        >
          <Form.Item name="goal" label="分析目标" rules={[{ required: true, message: '请输入分析目标!' }]}>
            <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况" rows={4} />
          </Form.Item>

          <Form.Item name="name" label="图表名称">
            <Input placeholder="请输入图表名称" />
          </Form.Item>

          <Form.Item name="chartType" label="图表类型">
            <Select
              options={[
                { value: '折线图', label: '折线图' },
                { value: '柱状图', label: '柱状图' },
                { value: '堆叠图', label: '堆叠图' },
                { value: '饼图', label: '饼图' },
                { value: '雷达图', label: '雷达图' },
              ]}
              placeholder="请选择或输入图表类型"
              showSearch
              allowClear
              mode="combobox"
            />
          </Form.Item>


          <Form.Item name="file" label="原始数据">
            <Upload name="file" showUploadList={true}>
              <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
            </Upload>
          </Form.Item>

          <Form.Item className="form-buttons">
            <Space>
              <Button type="primary" htmlType="submit" loading={submitting}>提交</Button>
              <Button htmlType="reset">重置</Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>

      {chart && (
        <Card className="chart-card" bordered={false}>
          <Title level={4}>分析结果</Title>
          <p>{chart?.genResult}</p>
          <ReactECharts option={option || {}} style={{ height: '400px', width: '100%' }} />
        </Card>
      )}
    </div>
  );
};

export default AddChart;
