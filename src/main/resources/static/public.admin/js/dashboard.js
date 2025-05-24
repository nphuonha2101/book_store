$(document).ready(function() {
  dashboard.init();
});

(function(exports, global) {
  let revenueChart;
  const statusLabels = {
    'ALL': 'Tất cả trạng thái',
    'DELIVERED': 'Đã giao hàng',
    'PROCESSING': 'Đang xử lý',
    'SHIPPING': 'Đang giao hàng',
    'CANCELLED': 'Đã huỷ',
    'PENDING': 'Chờ xác nhận',
    'FAILED': 'Thất bại',
  };

  exports.loadChartData = function() {
    const status = $('#orderStatus').val();
    const month = $('#monthFilter').val();

    $('.chart-container').addClass('opacity-50');

    fetch(`/admin/ajax/chart/order?status=${status}&month=${month}`)
        .then(response => response.json())
        .then(response => {
          if (response.success) {
            exports.renderChart(response.data, status, month);
          } else {
            console.error('Lỗi khi tải dữ liệu biểu đồ:', response.message);
          }
        })
        .catch(error => {
          console.error('Lỗi khi tải dữ liệu biểu đồ:', error);
        })
        .finally(() => {
          $('.chart-container').removeClass('opacity-50');
        });
  };

  exports.renderChart = function(data, status, month) {
    if (revenueChart) {
      revenueChart.destroy();
    }

    const labels = [];
    const values = [];
    const monthNames = [
      "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
      "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
    ];

    if (Object.keys(data).length > 1) {
      for (let i = 1; i <= 12; i++) {
        labels.push(monthNames[i-1]);
        values.push(data[i] || 0);
      }
    } else {
      const monthKey = parseInt(Object.keys(data)[0]);
      labels.push(monthNames[monthKey-1]);
      values.push(data[monthKey] || 0);
    }

    const totalRevenue = values.reduce((sum, current) => sum + current, 0);
    const chartType = 'bar';

    const ctx = document.getElementById('revenueChart').getContext('2d');
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'rgba(54, 162, 235, 0.8)');
    gradient.addColorStop(1, 'rgba(54, 162, 235, 0.2)');

    const statusText = statusLabels[status] || status;
    const monthText = month === 0 ? 'tất cả các tháng' : monthNames[month-1];
    const chartTitle = `Doanh thu (${statusText}) - ${monthText}`;

    revenueChart = new Chart(ctx, {
      type: chartType,
      data: {
        labels: labels,
        datasets: [{
          label: 'Doanh thu (VNĐ)',
          data: values,
          backgroundColor: gradient,
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1,
          borderRadius: 4,
          barPercentage: 0.7
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
          padding: {
            top: 20
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            grid: {
              color: 'rgba(0, 0, 0, 0.05)'
            },
            ticks: {
              callback: function(value) {
                return new Intl.NumberFormat('vi-VN', {
                  style: 'currency',
                  currency: 'VND',
                  maximumFractionDigits: 0
                }).format(value);
              },
              font: {
                size: 11
              }
            }
          },
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 11
              }
            }
          }
        },
        plugins: {
          legend: {
            display: false
          },
          title: {
            display: true,
            text: chartTitle,
            font: {
              size: 16
            },
            padding: {
              bottom: 20
            }
          },
          subtitle: {
            display: true,
            text: `Tổng doanh thu: ${new Intl.NumberFormat('vi-VN', {
              style: 'currency',
              currency: 'VND',
              maximumFractionDigits: 0
            }).format(totalRevenue)}`,
            padding: {
              bottom: 10
            },
            font: {
              size: 14,
              weight: 'bold'
            }
          },
          tooltip: {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',
            titleFont: {
              size: 13
            },
            bodyFont: {
              size: 13
            },
            padding: 12,
            callbacks: {
              label: function(context) {
                return 'Doanh thu: ' + new Intl.NumberFormat('vi-VN', {
                  style: 'currency',
                  currency: 'VND',
                  maximumFractionDigits: 0
                }).format(context.raw);
              }
            }
          }
        }
      }
    });
  };


  let statusPieChart;

  exports.loadStatusPieChart = function() {
    const month = $('#monthFilter').val();
    fetch(`/admin/ajax/chart/order-status?month=${month}`)
        .then(response => response.json())
        .then(response => {
          if (response.success) {
            exports.renderStatusPieChart(response.data);
          } else {
            console.error('Lỗi khi tải dữ liệu pie chart:', response.message);
          }
        })
        .catch(error => {
          console.error('Lỗi khi tải dữ liệu pie chart:', error);
        });
  };

  exports.renderStatusPieChart = function(data) {
    if (statusPieChart) {
      statusPieChart.destroy();
    }

    const labels = Object.keys(data).map(k => statusLabels[k] || k);
    const values = Object.values(data);
    const colors = [
      '#36A2EB', '#FFCE56', '#FF6384', '#4BC0C0', '#9966FF'
    ];

    const ctx = document.getElementById('statusPieChart').getContext('2d');
    statusPieChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [{
          data: values,
          backgroundColor: colors,
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom'
          },
          title: {
            display: true,
            text: 'Tỷ lệ doanh thu theo trạng thái đơn hàng'
          },
          tooltip: {
            callbacks: {
              label: function(context) {
                return `${context.label}: ${new Intl.NumberFormat('vi-VN', {
                  style: 'currency',
                  currency: 'VND',
                  maximumFractionDigits: 0
                }).format(context.raw)}`;
              }
            }
          }
        }
      }
    });
  }

  exports.init = function() {
    exports.loadChartData();
    exports.loadStatusPieChart();

    $('#filterBtn').off().on('click', function() {
      exports.loadChartData();
      exports.loadStatusPieChart();
    });
  };

})((window.dashboard = window.dashboard || {}), window);